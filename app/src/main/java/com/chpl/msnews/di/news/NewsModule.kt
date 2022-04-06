package com.chpl.msnews.di.news

import com.chpl.msnews.data.api.NewsService
import com.chpl.msnews.data.database.FavoritesDao
import com.chpl.msnews.di.application.ApplicationScope
import com.chpl.msnews.domain.mapper.NewsMapper
import com.chpl.msnews.domain.source.favorites.FavoritesInteractor
import com.chpl.msnews.domain.source.favorites.FavoritesRepository
import com.chpl.msnews.domain.source.favorites.FavoritesRepositoryImpl
import com.chpl.msnews.domain.source.favorites.FavoritesUseCase
import com.chpl.msnews.domain.source.news.NewsInteractor
import com.chpl.msnews.domain.source.news.NewsRepository
import com.chpl.msnews.domain.source.news.NewsRepositoryImpl
import com.chpl.msnews.domain.source.news.NewsUseCase
import com.chpl.msnews.ui.news.activity.NewsPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
internal interface NewsModule {

    @Binds
    fun bindsNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    fun bindsNewsUseCase(impl: NewsInteractor): NewsUseCase

    @Binds
    fun bindsFavoriteRepository(impl: FavoritesRepositoryImpl): FavoritesRepository

    @Binds
    fun bindsFavoriteUseCase(impl: FavoritesInteractor): FavoritesUseCase

    companion object {

        @Provides
        fun providesNewsPresenter(
            favoritesUseCase: FavoritesUseCase,
            newsUseCase: NewsUseCase,
            newsMapper: NewsMapper
        ) = NewsPresenter(
            favoritesUseCase = favoritesUseCase,
            newsUseCase = newsUseCase,
            newsMapper = newsMapper
        )

        @ApplicationScope
        @Provides
        fun providesNewsRepositoryImpl(newsService: NewsService) = NewsRepositoryImpl(newsService)

        @ApplicationScope
        @Provides
        fun providesNewsInteractor(newsRepository: NewsRepository, newsMapper: NewsMapper) =
            NewsInteractor(newsRepository, newsMapper)

        @ApplicationScope
        @Provides
        fun providesFavoritesRepositoryImpl(favoritesDao: FavoritesDao) = FavoritesRepositoryImpl(favoritesDao)

        @ApplicationScope
        @Provides
        fun providesFavoritesInteractor(newsRepository: FavoritesRepository, newsMapper: NewsMapper) =
            FavoritesInteractor(newsRepository, newsMapper)
    }
}