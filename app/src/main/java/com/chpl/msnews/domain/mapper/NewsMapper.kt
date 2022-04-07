package com.chpl.msnews.domain.mapper

import com.chpl.msnews.data.api.ArticleRaw
import com.chpl.msnews.data.api.ErrorRaw
import com.chpl.msnews.data.database.FavoritesEntity
import com.chpl.msnews.domain.model.Article
import com.chpl.msnews.domain.model.ArticleModel
import com.chpl.msnews.domain.source.favorites.FavoriteState
import com.chpl.msnews.ui.news.item.NewsItemUiModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class NewsMapper
@Inject constructor() {

    internal fun mapToArticleModelFromApi(articleRaw: ArticleRaw): ArticleModel {
        val article = Article(
            author = articleRaw.author ?: "",
            title = articleRaw.title,
            description = articleRaw.description,
            articleUrl = articleRaw.url,
            source = articleRaw.source,
            imageUrl = articleRaw.image ?: "",
            category = articleRaw.category,
            language = articleRaw.language,
            country = articleRaw.country,
            publishedAt = mapDate(articleRaw.publishedAt)
        )
        val articleId = article.hashCode()
        return ArticleModel(
            article = article,
            articleId = articleId
        )
    }

    internal fun mapToArticleModelFromDb(favoriteEntity: FavoritesEntity): ArticleModel {
        val article = Article(
            author = favoriteEntity.author ?: "",
            title = favoriteEntity.title,
            description = favoriteEntity.description,
            articleUrl = favoriteEntity.articleUrl,
            source = favoriteEntity.source,
            imageUrl = favoriteEntity.imageUrl ?: "",
            category = favoriteEntity.category,
            language = favoriteEntity.language,
            country = favoriteEntity.country,
            publishedAt = favoriteEntity.publishedAt
        )
        return ArticleModel(
            article = article,
            articleId = favoriteEntity.id
        )
    }

    internal fun mapToDb(account: String, articleModel: ArticleModel): FavoritesEntity {
        return FavoritesEntity(
            id = articleModel.articleId,
            userAccount = account,
            author = articleModel.article.author,
            title = articleModel.article.title,
            description = articleModel.article.description,
            articleUrl = articleModel.article.articleUrl,
            source = articleModel.article.source,
            imageUrl = articleModel.article.imageUrl,
            category = articleModel.article.category,
            language = articleModel.article.language,
            country = articleModel.article.country,
            publishedAt = articleModel.article.publishedAt,
        )

    }

    internal fun mapToNewsItemUiModel(
        articleModel: ArticleModel,
        favoriteState: FavoriteState,
        onFavoriteAction: ((Int) -> Unit),
        onItemAction: ((String) -> Unit)
    ) = NewsItemUiModel(
        id = articleModel.articleId,
        title = articleModel.article.title,
        author = articleModel.article.author,
        description = articleModel.article.description,
        imageUrl = articleModel.article.imageUrl,
        articleUrl = articleModel.article.articleUrl,
        onFavoriteAction = onFavoriteAction,
        onItemAction = onItemAction
    ).apply {
        this.favoriteState = favoriteState
    }

    private fun mapDate(date: String): Date? {
        return try {
            SimpleDateFormat("yyyy-MM-ddTHH:mm:ssXXXXXX", Locale.US).parse(date)
        } catch (e: Throwable) {
            Timber.e("Date parsing error, unknown date format: $date")
            null
        }
    }

    internal fun mapNewsError(error: ErrorRaw): String {
        return "code = ${error.code}\nmessage = ${error.message}\ncontext = ${error.context}"
    }
}