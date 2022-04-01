package com.chpl.msnews.di

import com.chpl.msnews.domain.CategoriesUseCase
import com.chpl.msnews.domain.CountriesUseCase
import com.chpl.msnews.domain.FiltersInteractor
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal interface FiltersModule {

    @Binds
    fun bindCategoriesUseCase(impl: FiltersInteractor): CategoriesUseCase

    @Binds
    fun bindCountriesUseCase(impl: FiltersInteractor): CountriesUseCase

    companion object {

        @Singleton
        @Provides
        fun providesFilterInteractor(): FiltersInteractor {
            return FiltersInteractor()
        }
    }
}