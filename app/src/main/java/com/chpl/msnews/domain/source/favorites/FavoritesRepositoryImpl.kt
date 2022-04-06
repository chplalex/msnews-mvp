package com.chpl.msnews.domain.source.favorites

import com.chpl.msnews.data.database.FavoritesDao
import com.chpl.msnews.data.database.FavoritesEntity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

internal class FavoritesRepositoryImpl
@Inject constructor(
    private val favoritesDao: FavoritesDao
) : FavoritesRepository {

    override fun getFavoriteList(account: String): Single<List<FavoritesEntity>> {
        return favoritesDao.queryFavoriteList(account).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Timber.d("CHPL -> queryFavoriteList() -> list.size = ${it.size}")
            }
            .doOnError {
                Timber.d("CHPL -> queryFavoriteList() -> error = $it")
            }
    }

    override fun getFavoriteItem(account: String, articleId: Int): Single<FavoritesEntity> {
        return favoritesDao.queryFavoriteItem(account, articleId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                Timber.d("CHPL -> getFavoriteItem() -> success -> $it")
            }
            .doOnError {
                Timber.d("CHPL -> queryFavoriteList() -> error = $it")
            }
    }

    override fun insertFavoriteItem(favorite: FavoritesEntity): Completable {
        return favoritesDao.insertFavoriteItem(favorite).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                Timber.d("CHPL -> insertFavoriteItem() -> complete")
            }
            .doOnError {
                Timber.d("CHPL -> insertFavoriteItem() -> error = $it")
            }
    }

    override fun deleteFavoriteItem(account: String, articleId: Int): Completable {
        return favoritesDao.deleteFavoriteItem(account, articleId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete {
                Timber.d("CHPL -> deleteFavoriteItem() -> complete")
            }
            .doOnError {
                Timber.d("CHPL -> deleteFavoriteItem() -> error = $it")
            }
    }
}