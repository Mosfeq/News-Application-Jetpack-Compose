package com.example.newsapplicationjetpackcompose.domain.use_case

import com.example.newsapplicationjetpackcompose.domain.model.Article
import com.example.newsapplicationjetpackcompose.domain.repository.NewsRepository
import com.example.newsapplicationjetpackcompose.util.UiState
import javax.inject.Inject

class AddNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    operator fun invoke(
        news: Article,
        response: (UiState<String>) -> Unit
    ){
        repository.addNews(news){
            response.invoke(it)
        }
    }
}