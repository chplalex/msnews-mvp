package com.chpl.news.domain.source.favorites

import com.chpl.news.domain.model.Article
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class FavoritesInteractor
@Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : FavoritesUseCase {

    override fun getFavorites(): Single<List<Article>> {
        return favoritesRepository.getFavorites()
    }

    override fun getFavoriteStatus(article: Article): Single<Boolean> {
        return favoritesRepository.getFavoriteStatus(article)
    }

    override fun switchFavoriteStatus(article: Article): Single<Boolean> {
        return favoritesRepository.switchFavoriteStatus(article)
    }
}