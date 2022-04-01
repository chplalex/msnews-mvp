package com.chpl.news.domain.source

import com.chpl.news.domain.model.Article
import io.reactivex.rxjava3.core.Single

interface NewsUseCase {

    fun getNews(keywords: String?, categories: String?, countries: String?): Single<List<Article>>
}