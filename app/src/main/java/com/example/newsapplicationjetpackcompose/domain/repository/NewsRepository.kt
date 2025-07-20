package com.example.newsapplicationjetpackcompose.domain.repository

import com.example.newsapplicationjetpackcompose.domain.model.Article
import com.example.newsapplicationjetpackcompose.domain.model.NewsResponse
import com.example.newsapplicationjetpackcompose.util.UiState
import retrofit2.Response

interface NewsRepository{

    suspend fun searchNews(searchQuery: String): Response<NewsResponse>

    fun addNews(news: Article, response: (UiState<String>) -> Unit)

    fun getSavedNews(result: (UiState<List<Article>>) -> Unit)

    fun removeNews(newsTitle: String)

    fun scheduleDailyNotifications()
}