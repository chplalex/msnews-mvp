package com.chpl.news.domain.source.favorites

import com.chpl.news.domain.model.Article
import io.reactivex.rxjava3.core.Single

interface FavoritesUseCase {

    fun getFavorites(): Single<List<Article>>

    fun getFavoriteState(article: Article): Single<FavoriteState>

    fun switchFavoriteState(article: Article): Single<FavoriteState>
}