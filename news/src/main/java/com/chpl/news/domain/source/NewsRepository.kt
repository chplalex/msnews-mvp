package com.chpl.news.domain.source

import com.chpl.news.data.response.GetNewsResponse
import io.reactivex.rxjava3.core.Single

internal interface NewsRepository {

    fun getNews(keywords: String?, categories: String?, countries: String?): Single<GetNewsResponse>
}