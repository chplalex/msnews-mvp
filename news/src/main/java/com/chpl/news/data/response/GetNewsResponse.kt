package com.chpl.news.data.response

import com.chpl.base.data.CategoryType
import com.chpl.base.data.CountryType
import com.chpl.base.data.LanguageType
import com.google.gson.annotations.SerializedName

internal data class GetNewsResponse(
    @SerializedName("pagination")
    val pagination: PaginationRaw? = null,
    @SerializedName("data")
    val data: List<ArticleRaw>? = null,
    @SerializedName("error")
    val error: ErrorRaw? = null
)

internal data class PaginationRaw(
    @SerializedName("limit")
    val limit: Int = 0,
    @SerializedName("offset")
    val offset: Int = 0,
    @SerializedName("count")
    val count: Int = 0,
    @SerializedName("total")
    val total: Int = 0
)

internal data class ArticleRaw(
    @SerializedName("author")
    val author: String? = null,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("source")
    val source: String = "",
    @SerializedName("image")
    val image: String? = null,
    @SerializedName("category")
    val category: CategoryType?,
    @SerializedName("language")
    val language: LanguageType?,
    @SerializedName("country")
    val country: CountryType?,
    @SerializedName("published_at")
    val publishedAt: String = "",
)

internal data class ErrorRaw(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("context")
    val context: String = ""
)