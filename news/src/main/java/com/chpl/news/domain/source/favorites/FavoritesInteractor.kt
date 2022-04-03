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

    override fun getFavoriteState(article: Article): Single<FavoriteState> {
        return favoritesRepository.getFavoriteState(article)
    }

    override fun switchFavoriteState(article: Article): Single<FavoriteState> {
        return favoritesRepository.switchFavoriteState(article)
    }
}