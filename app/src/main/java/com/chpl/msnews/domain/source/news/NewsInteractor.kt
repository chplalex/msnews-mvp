package com.chpl.msnews.domain.source.news

import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class NewsInteractor
@Inject constructor(
    private val newsRepository: NewsRepository,
    private val newsMapper: com.chpl.msnews.domain.mapper.NewsMapper
) : NewsUseCase {

    override fun getNews(keywords: String?, categories: String?, countries: String?): Single<List<com.chpl.msnews.domain.model.Article>> {
        return newsRepository.getNews(keywords = keywords, categories = categories, countries = countries)
            .doOnSuccess { response ->
                response.error?.let {
                    val message = newsMapper.mapNewsError(it)
                    throw Exception(message)
                }
            }
            .map { response -> response.data?.map { newsMapper.mapToArticleFromApi(it) } ?: emptyList() }
    }
}