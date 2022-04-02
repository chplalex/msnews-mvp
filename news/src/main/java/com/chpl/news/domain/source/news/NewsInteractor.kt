package com.chpl.news.domain.source.news

import com.chpl.news.domain.mapper.NewsMapper
import com.chpl.news.domain.model.Article
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class NewsInteractor
@Inject constructor(
    private val newsRepository: NewsRepository,
    private val newsMapper: NewsMapper
): NewsUseCase {

    override fun getNews(keywords: String?, categories: String?, countries: String?): Single<List<Article>> {
        return newsRepository.getNews(keywords = keywords, categories = categories, countries = countries)
            .doOnSuccess { response -> response.error?.let { throw Exception(it.message) } }
            .map { response -> response.data?.map { newsMapper.mapToArticle(it) } ?: emptyList() }
    }
}