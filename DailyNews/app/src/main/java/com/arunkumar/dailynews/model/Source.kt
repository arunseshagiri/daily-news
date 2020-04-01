package com.arunkumar.dailynews.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String
) : Parcelable