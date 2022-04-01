package com.chpl.msnews.di

import com.chpl.msnews.domain.CategoriesUseCase
import com.chpl.msnews.domain.CountriesUseCase
import com.chpl.msnews.ui.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @Provides
    fun providesMainPresenter(
        categoriesUseCase: CategoriesUseCase,
        countriesUseCase: CountriesUseCase
    ) = MainPresenter(
        categoriesUseCase = categoriesUseCase,
        countriesUseCase = countriesUseCase
    )
}