package com.chpl.msnews.data.model

import com.google.gson.annotations.SerializedName

enum class LanguageType(
    val label: String,
    val code: String
) {
    @SerializedName("Arabic")
    ARABIC("Arabic", "ar"),
    @SerializedName("German")
    GERMAN("German", "de"),
    @SerializedName("English")
    ENGLISH("English", "en"),
    @SerializedName("Spanish")
    SPANISH("Spanish", "es"),
    @SerializedName("French")
    FRENCH("French", "fr"),
    @SerializedName("Hebrew")
    HEBREW("Hebrew", "he"),
    @SerializedName("Italian")
    ITALIAN("Italian", "it"),
    @SerializedName("Dutch")
    DUTCH("Dutch", "nl"),
    @SerializedName("Norwegian")
    NORWEGIAN("Norwegian", "no"),
    @SerializedName("Portuguese")
    PORTUGUESE("Portuguese", "pt"),
    @SerializedName("Russian")
    RUSSIAN("Russian", "ru"),
    @SerializedName("Swedish")
    SWEDISH("Swedish", "se"),
    @SerializedName("Chinese")
    CHINESE("Chinese", "zh"),
}