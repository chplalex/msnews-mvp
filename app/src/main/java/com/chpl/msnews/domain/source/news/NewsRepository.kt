package com.chpl.msnews.domain.source.news

import com.chpl.msnews.data.api.GetNewsResponse
import io.reactivex.rxjava3.core.Single

internal interface NewsRepository {

    fun getNews(keywords: String?, categories: String?, countries: String?): Single<GetNewsResponse>
}