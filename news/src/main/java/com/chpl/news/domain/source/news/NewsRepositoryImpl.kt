package com.chpl.news.domain.source.news

import com.chpl.news.data.response.GetNewsResponse
import com.chpl.news.data.source.NewsService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class NewsRepositoryImpl
    @Inject constructor(
        private val newsService: NewsService
    ): NewsRepository {

    override fun getNews(keywords: String?, categories: String?, countries: String?): Single<GetNewsResponse> {
        return newsService.getNews(keywords = keywords, categories = categories, countries = countries)
            .subscribeOn(Schedulers.io())
    }
}