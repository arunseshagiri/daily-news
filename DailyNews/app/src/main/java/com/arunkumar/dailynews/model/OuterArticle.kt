package com.arunkumar.dailynews.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OuterArticle(
    @field:SerializedName("status") val status: String,
    @field:SerializedName("totalResults") val totalResults: Int,
    @field:SerializedName("articles") val articles: List<Articles>
) : Parcelable
