package com.chpl.news.domain.mapper

import com.chpl.news.data.response.ArticleRaw
import com.chpl.news.domain.model.Article
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

internal class NewsMapper
@Inject constructor() {

    fun mapArticle(articleRaw: ArticleRaw): Article {
        return Article(
        author = articleRaw.author,
        title = articleRaw.title,
        description = articleRaw.description,
        articleUrl = articleRaw.url,
        source = articleRaw.source,
        imageUrl = articleRaw.image,
        category = articleRaw.category,
        language = articleRaw.language,
        country = articleRaw.country,
        publishedAt = mapDate(articleRaw.publishedAt)
        )
    }

    private fun mapDate(date: String): Date? {
        return try {
            SimpleDateFormat("yyyy-MM-ddTHH:mm:ssXXXXXX", Locale.US).parse(date)
        } catch (e: Throwable) {
            Timber.d("Date parsing error, unknown date format: $date")
            null
        }
    }
}