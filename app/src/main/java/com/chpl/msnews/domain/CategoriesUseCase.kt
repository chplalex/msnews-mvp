package com.chpl.msnews.domain

import io.reactivex.rxjava3.core.Single

interface CategoriesUseCase {
    fun getCategories(): Single<List<String>>
}