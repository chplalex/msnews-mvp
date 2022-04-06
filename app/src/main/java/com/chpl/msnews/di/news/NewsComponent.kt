package com.chpl.msnews.di.news

import android.content.Context
import com.chpl.msnews.di.application.ApplicationScope
import com.chpl.msnews.di.source.ApiModule
import com.chpl.msnews.di.source.DbModule
import com.chpl.msnews.ui.news.activity.NewsActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ApiModule::class,
        DbModule::class,
        NewsModule::class
    ]
)
interface NewsComponent {

    fun inject(activity: NewsActivity)

    @Component.Factory
    interface Factory {

        fun createComponent(@BindsInstance context: Context): NewsComponent
    }
}