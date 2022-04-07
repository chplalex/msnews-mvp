package com.chpl.msnews.domain.source.favorites

import com.chpl.msnews.domain.model.ArticleModel
import io.reactivex.rxjava3.core.Single

interface FavoritesUseCase {

    fun getFavorites(account: String): Single<List<ArticleModel>>

    fun getFavoriteState(account: String, articleModel: ArticleModel): Single<FavoriteState>

    fun switchFavoriteState(account: String, articleModel: ArticleModel): Single<FavoriteState>
}