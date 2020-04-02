package com.arunkumar.dailynews.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.arunkumar.dailynews.R

const val ARTICLE_URL = "article_url"

fun Context.showProgressUI(imageView: ImageView) {
    val animationSet = AnimationSet(false)
    val animRotate: Animation = AnimationUtils.loadAnimation(this, R.anim.rotate)
    val animSlideIn: Animation = AnimationUtils.loadAnimation(this, R.anim.abc_slide_in_bottom)
    animationSet.addAnimation(animRotate)
    animationSet.addAnimation(animSlideIn)
    imageView.startAnimation(animationSet)
    imageView.visibility = View.VISIBLE
}

fun Context.hideProgressUI(imageView: ImageView) {
    val animSlideOut: Animation = AnimationUtils.loadAnimation(this, R.anim.abc_slide_out_bottom)
    imageView.startAnimation(animSlideOut)
    imageView.visibility = View.GONE
}