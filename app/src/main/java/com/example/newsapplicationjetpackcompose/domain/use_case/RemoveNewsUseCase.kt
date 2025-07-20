package com.example.newsapplicationjetpackcompose.domain.use_case

import com.example.newsapplicationjetpackcompose.domain.repository.NewsRepository
import javax.inject.Inject

class RemoveNewsUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    operator fun invoke(
        newsTitle: String
    ){
        repository.removeNews(newsTitle)
    }

}