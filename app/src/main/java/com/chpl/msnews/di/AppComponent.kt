package com.chpl.msnews.di

import com.chpl.msnews.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        FiltersModule::class,
        MainModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun createComponent(): AppComponent
    }
}