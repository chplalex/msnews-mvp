package com.chpl.news.domain.source.favorites

import com.chpl.news.domain.model.Article
import io.reactivex.rxjava3.core.Single

interface FavoritesUseCase {

    fun getFavorites(): Single<List<Article>>

    fun getFavoriteStatus(article: Article): Single<Boolean>

    fun switchFavoriteStatus(article: Article): Single<Boolean>
}