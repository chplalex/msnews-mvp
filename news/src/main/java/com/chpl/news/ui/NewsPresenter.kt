package com.chpl.news.ui

import com.chpl.base.ui.BasePresenter
import com.chpl.news.domain.source.NewsUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import moxy.InjectViewState
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class NewsPresenter
@Inject constructor(
    private val newsUseCase: NewsUseCase
) : BasePresenter<NewsView>() {

    private var keywords: String? = null
    private var categories: String? = null
    private var countries: String? = null
    private var articlesSource: ArticlesSourceType = ArticlesSourceType.FAVORITES

    private val favoritesSubject = PublishSubject.create<Unit>()
    private val newsSubject = PublishSubject.create<Unit>()

    private enum class ArticlesSourceType {
        FAVORITES,
        NEWS
    }

    fun init(keywords: String?, categories: String?, countries: String?) {
        this.keywords = keywords
        this.categories = categories
        this.countries = countries
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observeFavoritesSubject()
        observeNewsSubject()
        loadNews()
    }

    private fun observeFavoritesSubject() {
        // TODO("Not yet implemented")
    }

    private fun observeNewsSubject() {
        unsubscribeOnDestroy(
            newsSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.showProgress()
                }
                .observeOn(Schedulers.io())
                .switchMapSingle {
                    newsUseCase.getNews(keywords = keywords, categories = categories, countries = countries)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.hideProgress()
                }
                .subscribe({
                    viewState.showNewsArticles(it)
                }, {
                    Timber.e(it)
                })

        )
    }

    fun onButtonClicked() {
        when (articlesSource) {
            ArticlesSourceType.FAVORITES -> {
                loadFavorites()
                articlesSource = ArticlesSourceType.NEWS
            }
            ArticlesSourceType.NEWS -> {
                loadNews()
                articlesSource = ArticlesSourceType.FAVORITES
            }
        }
    }

    private fun loadNews() = newsSubject.onNext(Unit)

    private fun loadFavorites() = favoritesSubject.onNext(Unit)
}