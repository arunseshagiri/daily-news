package com.arunkumar.dailynews.api

import com.arunkumar.dailynews.model.Articles
import com.arunkumar.dailynews.model.OuterArticle
import io.reactivex.Single
import retrofit2.http.GET

interface ArticlesApi {
    @GET("news_1.json")
    fun articles(): Single<OuterArticle>
}