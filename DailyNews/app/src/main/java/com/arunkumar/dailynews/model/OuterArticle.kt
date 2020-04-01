package com.arunkumar.dailynews.model

import com.google.gson.annotations.SerializedName

data class OuterArticle(@SerializedName("products") val products: Map<String, Articles>)
