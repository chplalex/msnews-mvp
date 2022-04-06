package com.chpl.msnews.domain.source.favorites

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface FavoritesUseCase {

    fun getFavorites(account: String): Single<List<com.chpl.msnews.domain.model.Article>>

    fun getFavoriteState(account: String, article: com.chpl.msnews.domain.model.Article): Single<FavoriteState>

    fun switchFavoriteState(account: String, article: com.chpl.msnews.domain.model.Article): Completable
}