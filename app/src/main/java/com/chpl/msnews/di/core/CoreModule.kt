package com.chpl.msnews.di.core

import com.chpl.msnews.di.application.ApplicationScope
import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module
internal class CoreModule {

    @Provides
    @ApplicationScope
    fun provideGson(): Gson = Gson()
}