package com.chpl.news.domain.model

import com.chpl.base.data.CategoryType
import com.chpl.base.data.CountryType
import com.chpl.base.data.LanguageType
import java.util.Date

data class Article(
    val author: String,
    val title: String,
    val description: String,
    val articleUrl: String,
    val source: String,
    val imageUrl: String,
    val category: CategoryType?,
    val language: LanguageType?,
    val country: CountryType?,
    val publishedAt: Date? = null
)
