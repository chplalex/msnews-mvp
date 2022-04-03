package com.chpl.news.domain.source.favorites

import com.chpl.news.domain.model.Article
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class FavoritesRepositoryImpl
@Inject constructor() : FavoritesRepository {

    private val favorites = mutableListOf<Article>()

    override fun getFavorites(): Single<List<Article>> {
        return Single.just(favorites)
    }

    override fun getFavoriteState(article: Article): Single<FavoriteState> {
        val item = favorites.find { it == article }
        val state = if (item == null) FavoriteState.ENABLED_AND_OFF else FavoriteState.ENABLED_AND_ON
        return Single.just(state)
    }

    override fun switchFavoriteState(article: Article): Single<FavoriteState> {
        val isFavorite = favorites.find { it == article } != null
        val state = if (isFavorite) {
            // TODO здесь удаляем запись из БД и только в случае успеха удаляем из коллекции
            favorites.remove(article)
            FavoriteState.ENABLED_AND_OFF
        } else {
            // TODO здесь добавляем запись в БД и только в случае успеха добавляем в коллекцию
            favorites.add(article)
            FavoriteState.ENABLED_AND_ON
        }
        return Single.just(state)
    }
}