package com.chpl.msnews.ui

import com.chpl.base.ui.BasePresenter
import com.chpl.msnews.domain.CategoriesUseCase
import com.chpl.msnews.domain.CountriesUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MainPresenter
@Inject constructor(
    private val categoriesUseCase: CategoriesUseCase,
    private val countriesUseCase: CountriesUseCase
) : BasePresenter<MainView>() {

    private var keywords = ""
    private val countries = mutableSetOf<String>()
    private val categories = mutableSetOf<String>()
    private var isCategoriesCollapsed = true
    private var isCountriesCollapsed = true

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observeCategories()
        observeCountries()
    }

    private fun observeCategories() {
        unsubscribeOnDestroy(
            categoriesUseCase.getCategories()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewState.setCategories(it)
                    }, {
                        Timber.e(it)
                    }
                )
        )
    }

    private fun observeCountries() {
        unsubscribeOnDestroy(
            countriesUseCase.getCountries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        viewState.setCountries(it)
                    }, {
                        Timber.e(it)
                    }
                )
        )
    }

    fun onKeywordsChanged(keywords: String) {
        this.keywords = keywords.trim()
    }

    fun onCategoryClicked(category: String, checked: Boolean) {
        if (checked) {
            categories.add(category)
        } else {
            categories.remove(category)
        }
    }

    fun onCountryClicked(country: String, checked: Boolean) {
        if (checked) {
            countries.add(country)
        } else {
            countries.remove(country)
        }
    }

    fun onSearchButtonClicked() {
        val paramKeywords = keywords.ifEmpty { null }
        val paramCategories = categories.joinToString().ifEmpty { null }
        val paramCountries = countries.joinToString { countriesUseCase.getCountryCode(it) }.ifEmpty { null }
        viewState.openNewsScreen(paramKeywords, paramCategories, paramCountries)
    }

    fun onCategoriesClicked() {
        if (isCategoriesCollapsed)
            viewState.expandCategories()
        else
            viewState.collapseCategories()
        isCategoriesCollapsed = !isCategoriesCollapsed
    }

    fun onCountriesClicked() {
        if (isCountriesCollapsed)
            viewState.expandCountries()
        else
            viewState.collapseCountries()
        isCountriesCollapsed = !isCountriesCollapsed
    }
}