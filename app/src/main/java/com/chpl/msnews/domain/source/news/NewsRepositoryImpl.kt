package com.chpl.msnews.domain.source.news

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class NewsRepositoryImpl
@Inject constructor(
    private val newsService: com.chpl.msnews.data.api.NewsService
) : NewsRepository {

    override fun getNews(keywords: String?, categories: String?, countries: String?): Single<com.chpl.msnews.data.api.GetNewsResponse> {
        return newsService.getNews(keywords = keywords, categories = categories, countries = countries)
            .subscribeOn(Schedulers.io())
    }
}