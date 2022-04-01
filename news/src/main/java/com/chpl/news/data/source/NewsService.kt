package com.chpl.news.data.source

import com.chpl.news.data.response.GetNewsResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

internal interface NewsService {

    @GET(value = "/v1/news")
    fun getNews(
        @Query("keywords") keywords: String?,
        @Query("categories") categories: String?,
        @Query("countries") countries: String?,
    ): Single<GetNewsResponse>
}