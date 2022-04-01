package com.chpl.news.di

import com.chpl.news.ui.NewsActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApiModule::class,
        NewsModule::class
    ]
)
interface NewsComponent {

    fun inject(activity: NewsActivity)

    @Component.Factory
    interface Factory {

        fun createComponent(): NewsComponent
    }
}