package com.chpl.msnews.domain.source.favorites

import com.chpl.msnews.data.database.FavoritesEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

internal interface FavoritesRepository {

    fun getFavoriteList(account: String): Single<List<com.chpl.msnews.data.database.FavoritesEntity>>

    fun getFavoriteItem(account: String, articleId: Int): Single<com.chpl.msnews.data.database.FavoritesEntity>

    fun insertFavoriteItem(favorite: com.chpl.msnews.data.database.FavoritesEntity): Completable

    fun deleteFavoriteItem(account: String, articleId: Int): Completable
}