package com.arunkumar.dailynews.model

import android.os.Parcelable
import com.arunkumar.dailynews.utils.StringToLocalDateTimeElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

@Parcelize
data class Articles(
    @field:SerializedName("source") val source: Source,
    @field:SerializedName("author") val author: String,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("description") val description: String,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("urlToImage") val urlToImage: String,
    @field:SerializedName("publishedAt") @JsonAdapter(StringToLocalDateTimeElement::class) val publishedAt: LocalDateTime,
    @field:SerializedName("content") val content: String
) : Parcelable