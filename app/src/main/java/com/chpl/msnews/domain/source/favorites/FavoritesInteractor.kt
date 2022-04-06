package com.chpl.msnews.domain.source.favorites

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class FavoritesInteractor
@Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val newsMapper: com.chpl.msnews.domain.mapper.NewsMapper
) : FavoritesUseCase {

    override fun getFavorites(account: String): Single<List<com.chpl.msnews.domain.model.Article>> {
        return favoritesRepository.getFavoriteList(account)
            .map { response -> response.map { newsMapper.mapToArticleFromDb(it) } }
    }

    override fun getFavoriteState(account: String, article: com.chpl.msnews.domain.model.Article): Single<FavoriteState> {
        val articleId = article.hashCode()
        return favoritesRepository.getFavoriteItem(account, articleId)
            .map { FavoriteState.ENABLED_AND_ON }
    }

    override fun switchFavoriteState(account: String, article: com.chpl.msnews.domain.model.Article): Completable {
        val articleId = article.hashCode()
        return favoritesRepository.deleteFavoriteItem(account, articleId)
            .onErrorResumeNext {
                if (it is NoSuchElementException) {
                    val favoriteEntity = newsMapper.mapToDb(article, account)
                    favoritesRepository.insertFavoriteItem(favoriteEntity)
                } else {
                    Completable.error(it)
                }
            }
    }
}