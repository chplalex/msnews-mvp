package com.chpl.msnews.ui.news.activity

import com.chpl.msnews.domain.mapper.NewsMapper
import com.chpl.msnews.domain.model.ArticleModel
import com.chpl.msnews.domain.source.favorites.FavoriteState
import com.chpl.msnews.domain.source.favorites.FavoritesUseCase
import com.chpl.msnews.domain.source.news.NewsUseCase
import com.chpl.msnews.ui.news.item.NewsItemUiModel
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

    private var articleModels = mutableListOf<ArticleModel>()
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
                    favoritesUseCase.getFavoriteState(account, articleModels[position])
                        .map { state -> position to state }
                }
                .filter {
                    val position = it.first
                    val state = it.second
                    val filter = uiModels[position].favoriteState != state
                    filter
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
                    val articleModel = articleModels.find { it.articleId == itemId }
                        ?: throw IllegalArgumentException("Item with id == $itemId not found")
                    favoritesUseCase.switchFavoriteState(account, articleModel)
                        .map { state ->
                            val position = articleModels.indexOf(articleModel)
                            position to state
                        }
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
                articleModels.removeAt(position)
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
                        .map {
                            articlesSource = ArticlesSourceType.NEWS
                            articleModels.clear()
                            articleModels.addAll(it)
                            val state = when (articlesSource) {
                                ArticlesSourceType.NEWS -> FavoriteState.ENABLED_AND_OFF
                                ArticlesSourceType.FAVORITES -> FavoriteState.ENABLED_AND_ON
                            }
                            articleModels.map { articleModel ->
                                newsMapper.mapToNewsItemUiModel(
                                    articleModel = articleModel,
                                    favoriteState = state,
                                    onFavoriteAction = { switchFavoriteState(articleModel.articleId) },
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
                    favoritesUseCase.getFavorites(account).map {
                        articlesSource = ArticlesSourceType.FAVORITES
                        articleModels.clear()
                        articleModels.addAll(it)
                        articleModels.map { articleModel ->
                            newsMapper.mapToNewsItemUiModel(
                                articleModel = articleModel,
                                favoriteState = FavoriteState.ENABLED_AND_ON,
                                onFavoriteAction = { switchFavoriteState(articleModel.articleId) },
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