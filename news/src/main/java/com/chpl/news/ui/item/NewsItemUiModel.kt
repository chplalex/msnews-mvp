package com.chpl.news.ui.item

import com.chpl.news.domain.source.favorites.FavoriteState

data class NewsItemUiModel(
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val onFavoriteAction: ((Int) -> Unit)? = null
) {
    var favoriteState: FavoriteState = FavoriteState.DISABLED
}
