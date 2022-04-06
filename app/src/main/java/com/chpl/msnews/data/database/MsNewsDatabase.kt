package com.chpl.msnews.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import java.util.concurrent.Executors

private const val MS_NEWS_DB_NAME = "msnews.db"

@Database(entities = [FavoritesEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MsNewsDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    companion object {

        @Volatile
        private var instance: MsNewsDatabase? = null

        private val LOCK = Any()

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, MsNewsDatabase::class.java, MS_NEWS_DB_NAME)
                .fallbackToDestructiveMigration()
                .setQueryCallback(
                    { sqlQuery, bindArgs ->
                        println("SQL Query: $sqlQuery SQL Args: $bindArgs")
                    },
                    Executors.newSingleThreadExecutor()
                )
                .build()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}