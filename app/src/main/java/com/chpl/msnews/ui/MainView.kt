package com.chpl.msnews.ui

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface MainView: MvpView {

    fun setCategories(categories: List<String>)

    fun setCountries(countries: List<String>)

    fun openNewsScreen(keywords: String?, categories: String?, countries: String?)

    fun expandCategories()

    fun collapseCategories()

    fun expandCountries()

    fun collapseCountries()

    fun showStatusLoggedOut()

    fun showStatusLoggedIn(name: String?)

    fun signIn()

    fun singOut()

    fun showError(it: Throwable)
}