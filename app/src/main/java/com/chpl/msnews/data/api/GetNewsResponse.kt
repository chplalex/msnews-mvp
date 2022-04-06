package com.chpl.msnews.data.api

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
    val category: com.chpl.msnews.data.model.CategoryType?,
    @SerializedName("language")
    val language: com.chpl.msnews.data.model.LanguageType?,
    @SerializedName("country")
    val country: com.chpl.msnews.data.model.CountryType?,
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