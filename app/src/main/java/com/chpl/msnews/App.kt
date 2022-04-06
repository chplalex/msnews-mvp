package com.chpl.msnews

import android.app.Application
import com.chpl.msnews.di.application.ApplicationComponent
import com.chpl.msnews.di.application.DaggerApplicationComponent

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.factory().createComponent(this).inject(this)
    }
}