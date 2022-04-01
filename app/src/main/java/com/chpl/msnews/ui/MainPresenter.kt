package com.chpl.msnews.ui

import com.chpl.base.ui.BasePresenter
import com.chpl.msnews.domain.CategoriesUseCase
import com.chpl.msnews.domain.CountriesUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.InjectViewState
import moxy.MvpPresenter
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class MainPresenter
@Inject constructor(
    private val categoriesUseCase: CategoriesUseCase,
    private val countriesUseCase: CountriesUseCase
) : BasePresenter<MainView>() {

    private var keywords = ""
    private val countries = setOf<String>()
    private val categories = setOf<String>()

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
            categories.plus(category)
        } else {
            categories.minus(category)
        }
    }

    fun onCountryClicked(country: String, checked: Boolean) {
        if (checked) {
            countries.plus(country)
        } else {
            countries.minus(country)
        }
    }

    fun onSearchButtonClicked() {
        val paramKeywords = keywords.trim().ifEmpty { null }
        val paramCategories = countries.joinToString().ifEmpty { null }
        val paramCountries = categories.joinToString().ifEmpty { null }
        viewState.openNewsScreen(paramKeywords, paramCategories, paramCountries)
    }
}