package com.chpl.news.ui.activity

import com.chpl.base.ui.BasePresenter
import com.chpl.news.domain.mapper.NewsMapper
import com.chpl.news.domain.model.Article
import com.chpl.news.domain.source.favorites.FavoritesUseCase
import com.chpl.news.domain.source.news.NewsUseCase
import com.chpl.news.ui.item.NewsItemUiModel
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
    private val favoritesUseCase: FavoritesUseCase,
    private val newsUseCase: NewsUseCase,
    private val newsMapper: NewsMapper
) : BasePresenter<NewsView>() {

    private var keywords: String? = null
    private var categories: String? = null
    private var countries: String? = null
    private var articlesSource: ArticlesSourceType = ArticlesSourceType.FAVORITES

    private val favoriteStatusSubject = PublishSubject.create<Int>()
    private val favoritesLoadSubject = PublishSubject.create<Unit>()
    private val newsLoadSubject = PublishSubject.create<Unit>()

    private var articles = emptyList<Article>()
    private var newsItemUiModels = emptyList<NewsItemUiModel>()

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
        observeFavoriteStatusSubject()
        observeFavoritesLoadSubject()
        observeNewsLoadSubject()
        loadNews()
    }

    private fun observeFavoriteStatusSubject() {
        unsubscribeOnDestroy(
            favoriteStatusSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.showProgress()
                }
                .observeOn(Schedulers.io())
                .switchMapSingle { itemId ->
                    val article = articles.find { it.hashCode() == itemId }
                        ?: throw IllegalArgumentException("Item with hashCode == $itemId not found")
                    favoritesUseCase.switchFavoriteStatus(article).map { isFavorite -> article to isFavorite }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { viewState.hideProgress() }
                .subscribe({ pair ->
                    val uiModel = newsItemUiModels.find { uiModel -> uiModel.id == pair.first.hashCode() }
                        ?: throw IllegalArgumentException("Item with hashCode == ${pair.first.hashCode()} not found")
                    uiModel.isFavorite = pair.second
                    val position = newsItemUiModels.indexOf(uiModel)
                    viewState.updateNewsItem(position)
                }, {
                    Timber.e(it)
                })

        )
    }

    private fun observeFavoritesLoadSubject() {
        unsubscribeOnDestroy(
            favoritesLoadSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.showProgress()
                }
                .observeOn(Schedulers.io())
                .switchMapSingle {
                    favoritesUseCase.getFavorites().map { articles ->
                        this.articles = articles
                        articles.map { article -> newsMapper.mapToNewsItemUiModel(article, true) }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.hideProgress()
                }
                .subscribe({
                    viewState.showNewsItems(it)
                }, {
                    Timber.e(it)
                })
        )
    }

    private fun observeNewsLoadSubject() {
        unsubscribeOnDestroy(
            newsLoadSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.showProgress()
                }
                .observeOn(Schedulers.io())
                .switchMapSingle {
                    newsUseCase.getNews(keywords = keywords, categories = categories, countries = countries)
                        .map { articles ->
                            this.articles = articles
                            articles.map { article -> newsMapper.mapToNewsItemUiModel(article, false) }
                        }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.hideProgress()
                }
                .subscribe({
                    viewState.showNewsItems(it)
                    newsItemUiModels = it
                }, {
                    Timber.e(it)
                })

        )
    }

    fun onButtonClicked() {
        articlesSource = when (articlesSource) {
            ArticlesSourceType.FAVORITES -> {
                loadFavorites()
                ArticlesSourceType.NEWS
            }
            ArticlesSourceType.NEWS -> {
                loadNews()
                ArticlesSourceType.FAVORITES
            }
        }
    }

    fun onFavoriteIconClicked(itemId: Int) = favoriteStatusSubject.onNext(itemId)

    private fun loadNews() = newsLoadSubject.onNext(Unit)

    private fun loadFavorites() = favoritesLoadSubject.onNext(Unit)
}