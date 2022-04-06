package com.chpl.msnews.di.main

import com.chpl.msnews.ui.main.MainActivity
import dagger.Component

@Component(
    modules = [
        MainModule::class
    ]
)
interface MainComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun createComponent(): MainComponent
    }
}