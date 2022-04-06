package com.chpl.msnews.di.main

import com.chpl.msnews.domain.CategoriesUseCase
import com.chpl.msnews.domain.CountriesUseCase
import com.chpl.msnews.domain.FiltersInteractor
import com.chpl.msnews.ui.main.MainPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal interface MainModule {

    @Binds
    fun bindCategoriesUseCase(impl: FiltersInteractor): CategoriesUseCase

    @Binds
    fun bindCountriesUseCase(impl: FiltersInteractor): CountriesUseCase

    companion object {

        @Provides
        fun providesMainPresenter(
            categoriesUseCase: CategoriesUseCase,
            countriesUseCase: CountriesUseCase
        ) = MainPresenter(
            categoriesUseCase = categoriesUseCase,
            countriesUseCase = countriesUseCase
        )

        @Provides
        fun providesFilterInteractor() = FiltersInteractor()
    }
}