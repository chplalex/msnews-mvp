package com.chpl.msnews.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteItem(favorite: FavoritesEntity): Single<Long>

    @Query("DELETE FROM favorites WHERE user_account = :account AND id = :articleId")
    fun deleteFavoriteItem(account: String, articleId: Int): Single<Int>

    @Query("SELECT * FROM favorites WHERE user_account = :account AND id = :articleId")
    fun queryFavoriteItem(account: String, articleId: Int): Single<FavoritesEntity>

    @Query("SELECT * FROM favorites WHERE user_account = :account AND id = :articleId")
    fun queryFavoriteItems(account: String, articleId: Int): Single<List<FavoritesEntity>>

    @Query("SELECT * FROM favorites WHERE user_account = :account")
    fun queryFavoriteList(account: String): Single<List<FavoritesEntity>>
}