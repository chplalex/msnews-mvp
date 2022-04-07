package com.chpl.msnews.domain.source.news

import com.chpl.msnews.data.api.GetNewsResponse
import com.chpl.msnews.data.api.NewsService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

internal class NewsRepositoryImpl
@Inject constructor(
    private val newsService: NewsService
) : NewsRepository {

    override fun getNews(keywords: String?, categories: String?, countries: String?): Single<GetNewsResponse> {
        return newsService.getNews(keywords = keywords, categories = categories, countries = countries)
            .subscribeOn(Schedulers.io())
    }
}