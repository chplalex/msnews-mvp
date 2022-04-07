package com.chpl.msnews.domain.source.news

import com.chpl.msnews.domain.model.ArticleModel
import io.reactivex.rxjava3.core.Single

interface NewsUseCase {

    fun getNews(keywords: String?, categories: String?, countries: String?): Single<List<ArticleModel>>
}