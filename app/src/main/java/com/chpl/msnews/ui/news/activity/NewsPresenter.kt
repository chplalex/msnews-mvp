package com.chpl.msnews.ui.news.activity

import com.chpl.msnews.ui.news.item.NewsItemUiModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import moxy.InjectViewState
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class NewsPresenter
@Inject constructor(
    private val favoritesUseCase: com.chpl.msnews.domain.source.favorites.FavoritesUseCase,
    private val newsUseCase: com.chpl.msnews.domain.source.news.NewsUseCase,
    private val newsMapper: com.chpl.msnews.domain.mapper.NewsMapper
) : com.chpl.msnews.ui.base.BasePresenter<NewsView>() {

    private var keywords: String? = null
    private var categories: String? = null
    private var countries: String? = null
    private var account: String = ""
    private var articlesSource: ArticlesSourceType = ArticlesSourceType.NEWS

    private val checkFavoriteStateSubject = PublishSubject.create<Int>()
    private val loadFavoritesSubject = PublishSubject.create<Unit>()
    private val loadNewsSubject = PublishSubject.create<Unit>()
    private val switchFavoriteStateSubject = PublishSubject.create<Int>()

    private var articles = emptyList<com.chpl.msnews.domain.model.Article>()
    private var uiModels = mutableListOf<NewsItemUiModel>()

    private enum class ArticlesSourceType {
        FAVORITES,
        NEWS
    }

    fun init(keywords: String?, categories: String?, countries: String?, account: String?) {
        this.keywords = keywords
        this.categories = categories
        this.countries = countries
        this.account = account ?: ""
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        observeCheckFavoriteState()
        observeLoadFavorites()
        observeLoadNews()
        observeSwitchFavoriteState()
        loadNews()
    }

    private fun observeCheckFavoriteState() {
        unsubscribeOnDestroy(
            checkFavoriteStateSubject
                .switchMapSingle { position ->
                    favoritesUseCase.getFavoriteState(account, articles[position])
                        .map { state -> position to state }
                }
                .filter {
                    val position = it.first
                    val state = it.second
                    uiModels[position].favoriteState != state
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val position = it.first
                    val state = it.second
                    uiModels[position].favoriteState = state
                    viewState.updateNewsItem(position)
                }, {
                    Timber.e(it)
                })
        )
    }

    private fun observeSwitchFavoriteState() {
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
                    val newState = uiModels[position].favoriteState.switch()
                    favoritesUseCase.switchFavoriteState(account, article)
                        .andThen(Single.just(position to newState))
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

    private fun handleFavoriteState(position: Int, newState: com.chpl.msnews.domain.source.favorites.FavoriteState) {
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
                            val state = when (articlesSource) {
                                ArticlesSourceType.NEWS -> com.chpl.msnews.domain.source.favorites.FavoriteState.ENABLED_AND_OFF
                                ArticlesSourceType.FAVORITES -> com.chpl.msnews.domain.source.favorites.FavoriteState.ENABLED_AND_ON
                            }
                            articles.map { article ->
                                newsMapper.mapToNewsItemUiModel(
                                    article = article,
                                    favoriteState = state,
                                    onFavoriteAction = { switchFavoriteState(article.hashCode()) },
                                    onItemAction = { url -> viewState.openExternalBrowser(url) }
                                )
                            }
                        }
                }
                .doOnNext {
                    uiModels.clear()
                    uiModels.addAll(it)
                    if (account.isNotEmpty()) {
                        uiModels.forEachIndexed { index, _ -> checkFavoriteState(index) }
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    viewState.hideProgress()
                    viewState.showNewsItems(uiModels)
                    if (account.isEmpty()) {
                        viewState.hideFavorites()
                    } else {
                        viewState.showFavorites()
                    }
                }, {
                    viewState.hideProgress()
                    viewState.showError(it)
                    Timber.e(it)
                })

        )
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
                    favoritesUseCase.getFavorites(account).map { articles ->
                        this.articlesSource = ArticlesSourceType.FAVORITES
                        this.articles = articles
                        articles.map { article ->
                            newsMapper.mapToNewsItemUiModel(
                                article = article,
                                favoriteState = com.chpl.msnews.domain.source.favorites.FavoriteState.ENABLED_AND_ON,
                                onFavoriteAction = { switchFavoriteState(article.hashCode()) },
                                onItemAction = { url -> viewState.openExternalBrowser(url) }
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

    fun onButtonClicked() {
        when (articlesSource) {
            ArticlesSourceType.FAVORITES -> loadNews()
            ArticlesSourceType.NEWS -> loadFavorites()
        }
    }

    private fun loadFavorites() = loadFavoritesSubject.onNext(Unit)

    private fun loadNews() = loadNewsSubject.onNext(Unit)

    private fun switchFavoriteState(itemId: Int) = switchFavoriteStateSubject.onNext(itemId)

    private fun checkFavoriteState(index: Int) = checkFavoriteStateSubject.onNext(index)
}

private fun com.chpl.msnews.domain.source.favorites.FavoriteState.switch(): com.chpl.msnews.domain.source.favorites.FavoriteState = if (this == com.chpl.msnews.domain.source.favorites.FavoriteState.ENABLED_AND_ON) {
    com.chpl.msnews.domain.source.favorites.FavoriteState.ENABLED_AND_OFF
} else {
    com.chpl.msnews.domain.source.favorites.FavoriteState.ENABLED_AND_ON
}
