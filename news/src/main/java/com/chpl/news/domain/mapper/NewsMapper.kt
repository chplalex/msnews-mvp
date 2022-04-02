package com.chpl.news.domain.mapper

import com.chpl.news.data.response.ArticleRaw
import com.chpl.news.domain.model.Article
import com.chpl.news.ui.item.NewsItemUiModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class NewsMapper
@Inject constructor() {

    internal fun mapToArticle(articleRaw: ArticleRaw): Article {
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

    internal fun mapToNewsItemUiModel(article: Article, isFavoriteOn: Boolean): NewsItemUiModel {
        return NewsItemUiModel(
            id = article.hashCode(),
            title = article.title,
            author = article.author,
            description = article.description,
            imageUrl = article.imageUrl,
            isFavorite = isFavoriteOn
        )
    }

    private fun mapDate(date: String): Date? {
        return try {
            SimpleDateFormat("yyyy-MM-ddTHH:mm:ssXXXXXX", Locale.US).parse(date)
        } catch (e: Throwable) {
            Timber.e("Date parsing error, unknown date format: $date")
            null
        }
    }
}