package com.chpl.news.ui.item

internal interface NewsItemView {

    var isFavoriteOn: Boolean

    fun setFavoriteIconOn()

    fun setFavoriteIconOff()
}