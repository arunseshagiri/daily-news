package com.arunkumar.dailynews.api

import com.arunkumar.dailynews.model.OuterArticle
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers.io

class ArticlesApiService(private val retrofitBuilder: ArticlesRetrofitBuilder) {
    fun articles(country: String?): Single<OuterArticle> = retrofitBuilder.getApi().articles(1, country).subscribeOn(io())
}