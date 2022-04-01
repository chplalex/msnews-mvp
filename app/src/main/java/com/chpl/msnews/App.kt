package com.chpl.msnews

import android.app.Application
import com.chpl.msnews.di.AppComponent
import com.chpl.msnews.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().createComponent()
    }
}