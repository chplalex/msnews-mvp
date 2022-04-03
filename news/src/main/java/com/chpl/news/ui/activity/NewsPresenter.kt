package com.chpl.news.ui.activity

import com.chpl.base.ui.BasePresenter
import com.chpl.news.domain.mapper.NewsMapper
import com.chpl.news.domain.model.Article
import com.chpl.news.domain.source.favorites.FavoriteState
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
    private var articlesSource: ArticlesSourceType = ArticlesSourceType.NEWS

    private val loadFavoritesSubject = PublishSubject.create<Unit>()
    private val loadNewsSubject = PublishSubject.create<Unit>()
    private val switchFavoriteStateSubject = PublishSubject.create<Int>()

    private var articles = emptyList<Article>()
    private var uiModels = mutableListOf<NewsItemUiModel>()

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
        observeLoadFavorites()
        observeLoadNews()
        observeFavoriteState()
        loadNews()
    }

    private fun observeFavoriteState() {
        unsubscribeOnDestroy(
            switchFavoriteStateSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.showProgress()
                }
                .observeOn(Schedulers.io())
                .switchMapSingle { itemId ->
                    val article = articles.find { it.hashCode() == itemId }
                        ?: throw IllegalArgumentException("Item with hashCode == $itemId not found")
                    val position = articles.indexOf(article)
                    favoritesUseCase.switchFavoriteState(article).map { newState -> position to newState }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ pair ->
                    viewState.hideProgress()
                    handleFavoriteState(pair.first, pair.second)
                }, {
                    viewState.hideProgress()
                    viewState.showError(it)
                    Timber.e(it)
                })

        )
    }

    private fun handleFavoriteState(position: Int, newState: FavoriteState) {
        when (articlesSource) {
            ArticlesSourceType.NEWS -> {
                uiModels[position].favoriteState = newState
                viewState.updateNewsItem(position)
            }
            ArticlesSourceType.FAVORITES -> {
                uiModels.removeAt(position)
                viewState.removeNewsItem(position)
            }
        }
    }

    private fun observeLoadFavorites() {
        unsubscribeOnDestroy(
            loadFavoritesSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.showProgress()
                }
                .observeOn(Schedulers.io())
                .switchMapSingle {
                    favoritesUseCase.getFavorites().map { articles ->
                        this.articlesSource = ArticlesSourceType.FAVORITES
                        this.articles = articles
                        articles.map { article ->
                            newsMapper.mapToNewsItemUiModel(
                                article = article,
                                favoriteState = FavoriteState.ENABLED_AND_ON,
                                onFavoriteAction = { switchFavoriteState(article.hashCode()) }
                            )
                        }
                    }
                }
                .doOnNext {
                    uiModels.clear()
                    uiModels.addAll(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.hideProgress()
                    viewState.showFavoritesNewsItems(uiModels)
                }, {
                    viewState.hideProgress()
                    viewState.showError(it)
                    Timber.e(it)
                })
        )
    }

    private fun observeLoadNews() {
        unsubscribeOnDestroy(
            loadNewsSubject
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    viewState.showProgress()
                }
                .observeOn(Schedulers.io())
                .switchMapSingle {
                    newsUseCase.getNews(keywords = keywords, categories = categories, countries = countries)
                        .map { articles ->
                            this.articlesSource = ArticlesSourceType.NEWS
                            this.articles = articles
                            articles.map { article ->
                                val state = when (articlesSource) {
                                    ArticlesSourceType.NEWS -> FavoriteState.ENABLED_AND_OFF
                                    ArticlesSourceType.FAVORITES -> FavoriteState.ENABLED_AND_ON
                                }
                                val onFavoriteAction = mapToFavoriteAction(state)
                                newsMapper.mapToNewsItemUiModel(
                                    article = article,
                                    favoriteState = state,
                                    onFavoriteAction = { onFavoriteAction(article.hashCode()) }
                                )
                            }
                        }
                }
                .doOnNext {
                    uiModels.clear()
                    uiModels.addAll(it)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.hideProgress()
                    viewState.showNewsItems(uiModels)
                }, {
                    viewState.hideProgress()
                    viewState.showError(it)
                    Timber.e(it)
                })

        )
    }

    private fun mapToFavoriteAction(favoriteState: FavoriteState): ((Int) -> Unit) {
        return when (articlesSource) {
            ArticlesSourceType.FAVORITES -> ::switchFavoriteState
            ArticlesSourceType.NEWS -> when (favoriteState) {
                FavoriteState.ENABLED_AND_ON,
                FavoriteState.ENABLED_AND_OFF -> ::switchFavoriteState
                FavoriteState.DISABLED -> ::authUser
            }
        }
    }

    fun onButtonClicked() {
        when (articlesSource) {
            ArticlesSourceType.FAVORITES -> loadNews()
            ArticlesSourceType.NEWS -> loadFavorites()
        }
    }

    private fun authUser(itemId: Int) {
        // nothing
    }

    private fun loadFavorites() = loadFavoritesSubject.onNext(Unit)

    private fun loadNews() = loadNewsSubject.onNext(Unit)

    private fun switchFavoriteState(itemId: Int) = switchFavoriteStateSubject.onNext(itemId)
}