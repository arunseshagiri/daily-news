package com.arunkumar.dailynews

import androidx.multidex.MultiDexApplication
import com.arunkumar.dailynews.components.DaggerMainActivityComponent
import com.arunkumar.dailynews.components.MainActivityComponent
import timber.log.Timber

class MainApp : MultiDexApplication() {

    lateinit var component: MainActivityComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        component = DaggerMainActivityComponent.builder().build()
    }

}