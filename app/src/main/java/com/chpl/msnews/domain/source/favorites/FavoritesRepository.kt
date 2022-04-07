package com.chpl.msnews.domain.source.favorites

import com.chpl.msnews.data.database.FavoritesEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

interface FavoritesRepository {

    fun getFavoriteList(account: String): Single<List<FavoritesEntity>>

    fun getFavoriteItem(account: String, articleId: Int): Single<FavoritesEntity>

    fun getFavoriteItems(account: String, articleId: Int): Single<List<FavoritesEntity>>

    fun insertFavoriteItem(favorite: FavoritesEntity): Single<FavoriteState>

    fun deleteFavoriteItem(account: String, articleId: Int): Single<FavoriteState>
}