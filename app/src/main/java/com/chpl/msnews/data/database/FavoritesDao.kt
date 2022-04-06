package com.chpl.msnews.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteItem(favorite: FavoritesEntity): Completable

    @Query("DELETE FROM favorites WHERE user_account = :account AND article_id = :articleId")
    fun deleteFavoriteItem(account: String, articleId: Int): Completable

    @Query("SELECT * FROM favorites WHERE user_account = :account AND article_id = :articleId LIMIT 1")
    fun queryFavoriteItem(account: String, articleId: Int): Single<FavoritesEntity>

    @Query("SELECT * FROM favorites WHERE user_account = :account")
    fun queryFavoriteList(account: String): Single<List<FavoritesEntity>>
}