package com.chpl.msnews.domain

import io.reactivex.rxjava3.core.Single

interface CountriesUseCase {

    fun getCountries(): Single<List<String>>

    fun getCountryCode(name: String): String
}