package com.arunkumar.dailynews

import com.arunkumar.dailynews.api.ArticlesApiService
import com.arunkumar.dailynews.model.Articles
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ArticleViewModel(private val articlesApiService: ArticlesApiService) {
    private var updateArticleList: PublishSubject<List<Articles>> = PublishSubject.create()
    private var showProgress: PublishSubject<Unit> = PublishSubject.create()
    private var hideProgress: PublishSubject<Unit> = PublishSubject.create()
    private var showError: PublishSubject<String> = PublishSubject.create()
    private var articleListSortedForRecent: PublishSubject<List<Articles>> =
        PublishSubject.create()
    private var articleListSortedForPopular: PublishSubject<List<Articles>> =
        PublishSubject.create()

    fun onCreate(country: String) {
        fetchArticles(country)
    }

    fun onStart(country: String) {
        fetchArticles(country)
    }

    fun updateArticleList(): PublishSubject<List<Articles>> = updateArticleList

    fun showProgress(): PublishSubject<Unit> = showProgress

    fun hideProgress(): PublishSubject<Unit> = hideProgress

    fun showError(): PublishSubject<String> = showError

    fun articleListSortedForRecent(): PublishSubject<List<Articles>> = articleListSortedForRecent

    fun articleListSortedForPopular(): PublishSubject<List<Articles>> = articleListSortedForPopular

    private fun fetchArticles(country: String) = articlesApiService
        .articles(country)
        .toObservable()
        .observeOn(mainThread())
        .doOnError { hideProgress().onNext(Unit) }
        .doOnComplete { hideProgress().onNext(Unit) }
        .doOnSubscribe { showProgress().onNext(Unit) }
        .subscribe(
            {
                updateArticleList().onNext(it.articles)
            },
            {
                it.printStackTrace()
                showError().onNext(it.message!!)
            }
        )

    fun sortBasedOnRecentArticle(articleList: List<Articles>) = Single
        .just(articleList)
        //.map { it -> it.sortedWith(compareByDescending { it.timeCreated }).toMutableList() }
        .subscribeOn(Schedulers.io())
        .observeOn(mainThread())
        .subscribe(
            {
                articleListSortedForRecent().onNext(it)
            },
            {
                showError().onNext(it.message!!)
            }
        )

    fun sortBasedOnPopularArticle(articleList: List<Articles>) = Single
        .just(articleList)
        //.map { it -> it.sortedWith(compareBy({ it.rank }, { it.timeCreated })).toMutableList() }
        .subscribeOn(Schedulers.io())
        .observeOn(mainThread())
        .subscribe(
            {
                articleListSortedForPopular().onNext(it)
            },
            {
                showError().onNext(it.message!!)
            }
        )

}