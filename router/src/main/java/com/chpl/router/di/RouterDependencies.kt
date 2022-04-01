package com.chpl.router.di

import com.chpl.router.Router

interface RouterDependencies {

    fun provideRouter(): Router
}