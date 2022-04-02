package com.chpl.news.ui.item

data class NewsItemUiModel(
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val imageUrl: String = "",
    var isFavorite: Boolean = false,
)
