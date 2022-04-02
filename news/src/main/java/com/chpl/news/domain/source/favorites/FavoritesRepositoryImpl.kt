package com.chpl.news.domain.source.favorites

import com.chpl.news.domain.model.Article
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class FavoritesRepositoryImpl
    @Inject constructor(): FavoritesRepository {

    private val favorites = mutableListOf<Article>()

    override fun getFavorites(): Single<List<Article>> {
        return Single.just(favorites)
    }

    override fun getFavoriteStatus(article: Article): Single<Boolean> {
        val isFavorite = favorites.find { it == article } != null
        return Single.just(isFavorite)
    }

    override fun switchFavoriteStatus(article: Article): Single<Boolean> {
        val favoriteArticle = favorites.find { it == article }
        val isFavorite = favoriteArticle != null
        if (isFavorite)
            favorites.remove(article)
        else
            favorites.add(article)
        return Single.just(!isFavorite)
    }
}