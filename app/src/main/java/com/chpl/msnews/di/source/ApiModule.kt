package com.chpl.msnews.di.source

import com.chpl.msnews.BuildConfig
import com.chpl.msnews.di.application.ApplicationScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

private const val API_KEY = "access_key"
private const val BASE_URL = "http://api.mediastack.com"

@Module
internal object ApiModule {

    @ApplicationScope
    @Provides
    fun provideRetrofit(): Retrofit {
        val okHttpBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpBuilder.addInterceptor(httpLoggingInterceptor)
        }
        okHttpBuilder.addInterceptor {
            val request = it.request()
            val url = request.url.newBuilder()
                .addQueryParameter(API_KEY, BuildConfig.API_KEY)
                .build()
            it.proceed(request.newBuilder().url(url).build())
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpBuilder.build())
            .build()
    }

    @ApplicationScope
    @Provides
    fun provideNewsService(retrofit: Retrofit): com.chpl.msnews.data.api.NewsService =
        retrofit.create(com.chpl.msnews.data.api.NewsService::class.java)
}