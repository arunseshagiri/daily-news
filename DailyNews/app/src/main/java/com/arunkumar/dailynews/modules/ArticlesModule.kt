package com.arunkumar.dailynews.modules

import com.arunkumar.dailynews.ArticleAdapter
import com.arunkumar.dailynews.ArticleViewModel
import com.arunkumar.dailynews.api.ArticlesApiService
import com.arunkumar.dailynews.api.ArticlesRetrofitBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ArticlesModule {

    @Provides
    fun provideRetrofitBuilder(): ArticlesRetrofitBuilder =
        ArticlesRetrofitBuilder()

    @Provides
    fun provideArticleApiService(retrofitBuilder: ArticlesRetrofitBuilder): ArticlesApiService =
        ArticlesApiService(retrofitBuilder)

    @Provides
    fun provideArticleViewModel(articlesApiService: ArticlesApiService): ArticleViewModel =
        ArticleViewModel(articlesApiService)

    @Provides
    fun provideArticleAdapter(): ArticleAdapter =
        ArticleAdapter(mutableListOf())

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}