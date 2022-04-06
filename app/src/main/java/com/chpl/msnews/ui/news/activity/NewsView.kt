package com.chpl.msnews.ui.news.activity

import com.chpl.msnews.ui.news.item.NewsItemUiModel
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView : MvpView {

    fun hideProgress()

    fun showProgress()

    fun showNewsItems(newsItems: List<NewsItemUiModel>)

    fun showFavoritesNewsItems(newsItems: List<NewsItemUiModel>)

    fun updateNewsItem(position: Int)

    fun removeNewsItem(position: Int)

    fun showError(it: Throwable)

    fun showFavorites()

    fun hideFavorites()

    fun openExternalBrowser(url: String)
}