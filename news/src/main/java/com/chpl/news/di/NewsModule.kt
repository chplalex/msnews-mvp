package com.chpl.news.di

import com.chpl.news.data.source.NewsService
import com.chpl.news.domain.mapper.NewsMapper
import com.chpl.news.domain.source.favorites.FavoritesInteractor
import com.chpl.news.domain.source.favorites.FavoritesRepository
import com.chpl.news.domain.source.favorites.FavoritesRepositoryImpl
import com.chpl.news.domain.source.favorites.FavoritesUseCase
import com.chpl.news.domain.source.news.NewsInteractor
import com.chpl.news.domain.source.news.NewsRepository
import com.chpl.news.domain.source.news.NewsRepositoryImpl
import com.chpl.news.domain.source.news.NewsUseCase
import com.chpl.news.ui.activity.NewsPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

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

        @Singleton
        @Provides
        fun providesNewsRepositoryImpl(newsService: NewsService) = NewsRepositoryImpl(newsService)

        @Singleton
        @Provides
        fun providesNewsInteractor(newsRepository: NewsRepository, newsMapper: NewsMapper) =
            NewsInteractor(newsRepository, newsMapper)

        @Singleton
        @Provides
        fun providesFavoritesRepositoryImpl() = FavoritesRepositoryImpl()

        @Singleton
        @Provides
        fun providesFavoritesInteractor(newsRepository: FavoritesRepository) = FavoritesInteractor(newsRepository)
    }
}