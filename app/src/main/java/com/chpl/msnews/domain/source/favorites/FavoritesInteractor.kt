package com.chpl.msnews.domain.source.favorites

import com.chpl.msnews.domain.mapper.NewsMapper
import com.chpl.msnews.domain.model.Article
import com.chpl.msnews.domain.model.ArticleModel
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

internal class FavoritesInteractor
@Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val newsMapper: NewsMapper
) : FavoritesUseCase {

    override fun getFavorites(account: String): Single<List<ArticleModel>> {
        return favoritesRepository.getFavoriteList(account)
            .map { response -> response.map { newsMapper.mapToArticleModelFromDb(it) } }
    }

    override fun getFavoriteState(account: String, articleModel: ArticleModel): Single<FavoriteState> {
        return favoritesRepository.getFavoriteItems(account, articleModel.articleId)
            .map {
                if (it.isEmpty()) {
                    FavoriteState.ENABLED_AND_OFF
                }
                else {
                    FavoriteState.ENABLED_AND_ON
                }
            }
    }

    override fun switchFavoriteState(account: String, articleModel: ArticleModel): Single<FavoriteState> {
        return favoritesRepository.getFavoriteItems(account, articleModel.articleId)
            .flatMap {
                if (it.isEmpty()) {
                    val entity = newsMapper.mapToDb(account, articleModel)
                    favoritesRepository.insertFavoriteItem(entity)
                } else {
                    favoritesRepository.deleteFavoriteItem(account, articleModel.articleId)
                }
            }
    }
}