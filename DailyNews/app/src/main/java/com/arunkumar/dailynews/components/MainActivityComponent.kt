package com.arunkumar.dailynews.components

import com.arunkumar.dailynews.MainActivity
import com.arunkumar.dailynews.modules.ArticlesModule
import dagger.Component

@Component(modules = [ArticlesModule::class])
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}