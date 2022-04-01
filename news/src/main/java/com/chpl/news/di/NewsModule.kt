package com.chpl.news.di

import com.chpl.news.data.source.NewsService
import com.chpl.news.domain.mapper.NewsMapper
import com.chpl.news.domain.source.NewsInteractor
import com.chpl.news.domain.source.NewsRepository
import com.chpl.news.domain.source.NewsRepositoryImpl
import com.chpl.news.domain.source.NewsUseCase
import com.chpl.news.ui.NewsPresenter
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

    companion object {

        @Provides
        fun providesNewsPresenter(newsUseCase: NewsUseCase) = NewsPresenter(newsUseCase)

        @Singleton
        @Provides
        fun providesNewsRepositoryImpl(newsService: NewsService) = NewsRepositoryImpl(newsService)

        @Singleton
        @Provides
        fun providesNewsInteractor(newsRepository: NewsRepository, newsMapper: NewsMapper) =
            NewsInteractor(newsRepository, newsMapper)
    }
}