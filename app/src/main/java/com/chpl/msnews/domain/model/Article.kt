package com.chpl.msnews.domain.model

import com.chpl.msnews.data.model.CategoryType
import com.chpl.msnews.data.model.CountryType
import com.chpl.msnews.data.model.LanguageType
import java.util.Date

data class Article(
    val author: String,
    val title: String,
    val description: String,
    val articleUrl: String,
    val source: String,
    val imageUrl: String,
    val category: com.chpl.msnews.data.model.CategoryType?,
    val language: com.chpl.msnews.data.model.LanguageType?,
    val country: com.chpl.msnews.data.model.CountryType?,
    val publishedAt: Date? = null
)
