package com.chpl.msnews.di.application

import android.content.Context
import dagger.BindsInstance
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @Provides
    @ApplicationScope
    fun provideApplication(context: Context) = context
}