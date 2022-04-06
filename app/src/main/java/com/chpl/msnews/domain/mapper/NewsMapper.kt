package com.chpl.msnews.domain.mapper

import com.chpl.msnews.domain.model.Article
import com.chpl.msnews.domain.source.favorites.FavoriteState
import com.chpl.msnews.ui.news.item.NewsItemUiModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class NewsMapper
@Inject constructor() {

    internal fun mapToArticleFromApi(articleRaw: com.chpl.msnews.data.api.ArticleRaw): Article {
        return Article(
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
    }

    internal fun mapToArticleFromDb(favoriteEntity: com.chpl.msnews.data.database.FavoritesEntity): Article {
        return Article(
            author = favoriteEntity.author,
            title = favoriteEntity.title,
            description = favoriteEntity.description,
            articleUrl = favoriteEntity.articleUrl,
            source = favoriteEntity.source,
            imageUrl = favoriteEntity.imageUrl,
            category = favoriteEntity.category,
            language = favoriteEntity.language,
            country = favoriteEntity.country,
            publishedAt = favoriteEntity.publishedAt
        )
    }

    internal fun mapToDb(article: Article, account: String): com.chpl.msnews.data.database.FavoritesEntity {
        return com.chpl.msnews.data.database.FavoritesEntity(
            articleId = article.hashCode(),
            userAccount = account,
            author = article.author,
            title = article.title,
            description = article.description,
            articleUrl = article.articleUrl,
            source = article.source,
            imageUrl = article.imageUrl,
            category = article.category,
            language = article.language,
            country = article.country,
            publishedAt = article.publishedAt,
        )

    }

    internal fun mapToNewsItemUiModel(
        article: Article,
        favoriteState: FavoriteState,
        onFavoriteAction: ((Int) -> Unit),
        onItemAction: ((String) -> Unit)
    ) = NewsItemUiModel(
        id = article.hashCode(),
        title = article.title,
        author = article.author,
        description = article.description,
        imageUrl = article.imageUrl,
        articleUrl = article.articleUrl,
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

    internal fun mapNewsError(error: com.chpl.msnews.data.api.ErrorRaw): String {
        return "code = ${error.code}\nmessage = ${error.message}\ncontext = ${error.context}"
    }
}