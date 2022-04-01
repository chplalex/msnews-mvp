package com.chpl.news.ui

import com.chpl.news.domain.model.Article
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface NewsView: MvpView {

    fun showNewsArticles(articles: List<Article>)

    fun showFavoritesArticles(articles: List<Article>)

    fun hideProgress()

    fun showProgress()
}