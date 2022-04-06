package com.chpl.msnews.di.application

import android.app.Application
import android.content.Context
import com.chpl.msnews.di.core.CoreModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ApplicationModule::class,
        CoreModule::class,
    ]
)
interface ApplicationComponent {

    fun inject(application: Application)

    @Component.Factory
    interface Factory {

        fun createComponent(@BindsInstance context: Context): ApplicationComponent
    }
}