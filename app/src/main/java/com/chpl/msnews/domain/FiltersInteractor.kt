package com.chpl.msnews.domain

import com.chpl.msnews.data.model.CategoryType
import com.chpl.msnews.data.model.CountryType
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class FiltersInteractor
@Inject constructor() : CategoriesUseCase, CountriesUseCase {

    private val categories = mutableListOf<String>()
    private val countries = mutableListOf<String>()

    init {
        categories.addAll(
            CategoryType.values().map { it.label }
        )
        countries.addAll(
            CountryType.values().map { it.label }
        )
    }

    override fun getCategories(): Single<List<String>> {
        return Single.just(categories)
    }

    override fun getCountries(): Single<List<String>> {
        return Single.just(countries)
    }

    override fun getCountryCode(name: String): String {
        val country = CountryType.values().find { it.label == name }
        return country?.code ?: ""
    }
}