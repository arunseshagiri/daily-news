package com.arunkumar.dailynews.api

import com.arunkumar.dailynews.model.OuterArticle
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesApi {
    @GET("top-headlines?country=in&pageSize=100&q=corona")
    fun articles(@Query("page") page: Int, @Query("country") country: String): Single<OuterArticle>
}