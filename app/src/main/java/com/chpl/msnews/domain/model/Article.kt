package com.chpl.msnews.domain.model

import com.chpl.msnews.data.model.CategoryType
import com.chpl.msnews.data.model.CountryType
import com.chpl.msnews.data.model.LanguageType
import java.util.Date

data class ArticleModel(
    val article: Article,
    val articleId: Int = 0
)

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
