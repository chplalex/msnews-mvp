package com.chpl.router.di

import com.chpl.router.Router
import com.chpl.router.RouterImpl
import dagger.Module
import dagger.Provides

@Module
internal class RouterModule {

    @Provides
    fun providesRouter(): Router = RouterImpl()
}