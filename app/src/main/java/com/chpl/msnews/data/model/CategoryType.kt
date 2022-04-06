package com.chpl.msnews.data.model

import com.google.gson.annotations.SerializedName

enum class CategoryType(
    val label: String
) {
    @SerializedName("general")
    GENERAL("general"),
    @SerializedName("business")
    BUSINESS("business"),
    @SerializedName("entertainment")
    ENTERTAINMENT("entertainment"),
    @SerializedName("health")
    HEALTH("health"),
    @SerializedName("science")
    SCIENCE("science"),
    @SerializedName("sports")
    SPORTS("sports"),
    @SerializedName("technology")
    TECHNOLOGY("technology"),
}