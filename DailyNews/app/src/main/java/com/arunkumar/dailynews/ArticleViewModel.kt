package com.arunkumar.dailynews

import com.arunkumar.dailynews.api.ArticlesApiService
import com.arunkumar.dailynews.model.Articles
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class ArticleViewModel(private val articlesApiService: ArticlesApiService) {
    private var updateArticleList: PublishSubject<MutableList<Articles>> = PublishSubject.create()
    private var showProgress: PublishSubject<Unit> = PublishSubject.create()
    private var hideProgress: PublishSubject<Unit> = PublishSubject.create()
    private var showError: PublishSubject<String> = PublishSubject.create()
    private var articleListSortedForRecent: PublishSubject<MutableList<Articles>> =
        PublishSubject.create()
    private var articleListSortedForPopular: PublishSubject<MutableList<Articles>> =
        PublishSubject.create()

    fun onCreate() {
        fetchArticles()
    }

    fun updateArticleList(): PublishSubject<MutableList<Articles>> = updateArticleList

    fun showProgress(): PublishSubject<Unit> = showProgress

    fun hideProgress(): PublishSubject<Unit> = hideProgress

    fun showError(): PublishSubject<String> = showError

    fun articleListSortedForRecent(): PublishSubject<MutableList<Articles>> =
        articleListSortedForRecent

    fun articleListSortedForPopular(): PublishSubject<MutableList<Articles>> =
        articleListSortedForPopular

    private fun fetchArticles() = articlesApiService
        .articles()
        .toObservable()
        .map { aa -> aa.products.values.toList() }
        .observeOn(mainThread())
        .doOnError { hideProgress().onNext(Unit) }
        .doOnComplete { hideProgress().onNext(Unit) }
        .doOnSubscribe { showProgress().onNext(Unit) }
        .map { it -> it.sortedWith(compareByDescending { it.timeCreated }).toMutableList() }
        .subscribe(
            {
                updateArticleList().onNext(it)
            },
            {
                showError().onNext(it.message!!)
            }
        )

    fun sortBasedOnRecentArticle(articleList: List<Articles>) = Single
        .just(articleList)
        .map { it -> it.sortedWith(compareByDescending { it.timeCreated }).toMutableList() }
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
        .map { it -> it.sortedWith(compareBy({ it.rank }, { it.timeCreated })).toMutableList() }
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