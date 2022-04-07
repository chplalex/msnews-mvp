package com.chpl.msnews.domain.source.favorites

import com.chpl.msnews.data.database.FavoritesDao
import com.chpl.msnews.data.database.FavoritesEntity
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class FavoritesRepositoryImpl
@Inject constructor(
    private val favoritesDao: FavoritesDao
) : FavoritesRepository {

    override fun getFavoriteList(account: String): Single<List<FavoritesEntity>> {
        return favoritesDao.queryFavoriteList(account).subscribeOn(Schedulers.io())
    }

    override fun getFavoriteItem(account: String, articleId: Int): Single<FavoritesEntity> {
        return favoritesDao.queryFavoriteItem(account, articleId).subscribeOn(Schedulers.io())
    }

    override fun getFavoriteItems(account: String, articleId: Int): Single<List<FavoritesEntity>> {
        return favoritesDao.queryFavoriteItems(account, articleId)
    }

    override fun insertFavoriteItem(favorite: FavoritesEntity): Single<FavoriteState> {
        return favoritesDao.insertFavoriteItem(favorite).subscribeOn(Schedulers.io())
            .map { FavoriteState.ENABLED_AND_ON }
    }

    override fun deleteFavoriteItem(account: String, articleId: Int): Single<FavoriteState> {
        return favoritesDao.deleteFavoriteItem(account, articleId).subscribeOn(Schedulers.io())
            .map { FavoriteState.ENABLED_AND_OFF }
    }
}