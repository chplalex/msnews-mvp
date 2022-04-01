package com.chpl.news.ui

import com.chpl.base.ui.BasePresenter
import com.chpl.news.domain.source.NewsInteractor
import com.chpl.news.domain.source.NewsUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import moxy.InjectViewState
import timber.log.Timber
import javax.inject.Inject

@InjectViewState
class NewsPresenter
@Inject constructor(
    private val newsUseCase: NewsUseCase
) : BasePresenter<NewsView>() {

    private var keywords: String? = null
    private var categories: String? = null
    private var countries: String? = null

    fun init(keywords: String?, categories: String?, countries: String?) {
        this.keywords = keywords
        this.categories = categories
        this.countries = countries
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadNews()
    }

    private fun loadNews() {
        unsubscribeOnDestroy(
            newsUseCase.getNews(keywords = keywords, categories = categories, countries = countries)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it.forEachIndexed { index, article ->
                        Timber.d("$index -> $article")
                    }
                }, {
                    Timber.e(it)
                })
        )
    }
}