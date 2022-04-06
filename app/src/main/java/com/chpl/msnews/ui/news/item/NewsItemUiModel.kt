package com.chpl.msnews.ui.news.item

import com.chpl.msnews.domain.source.favorites.FavoriteState

data class NewsItemUiModel(
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val articleUrl: String = "",
    val onFavoriteAction: ((Int) -> Unit)? = null,
    val onItemAction: ((String) -> Unit)? = null
) {
    var favoriteState: com.chpl.msnews.domain.source.favorites.FavoriteState = com.chpl.msnews.domain.source.favorites.FavoriteState.DISABLED
}
