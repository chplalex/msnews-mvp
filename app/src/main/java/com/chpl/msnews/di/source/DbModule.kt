package com.chpl.msnews.di.source

import android.content.Context
import com.chpl.msnews.data.database.MsNewsDatabase
import com.chpl.msnews.di.application.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
internal class DbModule {

    @ApplicationScope
    @Provides
    fun providesFavoritesDao(context: Context) = MsNewsDatabase.invoke(context).favoritesDao()
}