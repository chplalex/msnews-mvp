package com.chpl.msnews.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chpl.msnews.data.model.CategoryType
import com.chpl.msnews.data.model.CountryType
import com.chpl.msnews.data.model.LanguageType
import java.util.Date

@Entity(tableName = "favorites")
data class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "article_id")
    val articleId: Int = 0,
    @ColumnInfo(name = "user_account")
    val userAccount: String = "",
    @ColumnInfo(name = "author")
    val author: String = "",
    @ColumnInfo(name = "title")
    val title: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "article_url")
    val articleUrl: String = "",
    @ColumnInfo(name = "source")
    val source: String = "",
    @ColumnInfo(name = "image_url")
    val imageUrl: String = "",
    @ColumnInfo(name = "category")
    val category: CategoryType? = null,
    @ColumnInfo(name = "language")
    val language: LanguageType? = null,
    @ColumnInfo(name = "country")
    val country: CountryType? = null,
    @ColumnInfo(name = "published_at")
    val publishedAt: Date? = null
)
