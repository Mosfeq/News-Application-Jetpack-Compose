package com.example.newsapplicationjetpackcompose.data.remote.api

import com.example.newsapplicationjetpackcompose.domain.model.NewsResponse
import com.example.newsapplicationjetpackcompose.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        searchQuery: String,
        @Query("searchIn")
        searchIn: String = "title",
        @Query("language")
        language: String = "en",
        @Query("apiKey")
        apiKey: String = API_KEY
    ): Response<NewsResponse>

}