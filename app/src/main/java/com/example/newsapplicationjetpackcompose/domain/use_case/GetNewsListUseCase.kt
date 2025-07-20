package com.example.newsapplicationjetpackcompose.domain.use_case

import android.util.Log
import com.example.newsapplicationjetpackcompose.domain.model.NewsResponse
import com.example.newsapplicationjetpackcompose.domain.repository.NewsRepository
import com.example.newsapplicationjetpackcompose.util.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class GetNewsListUseCase @Inject constructor(
    private val repository: NewsRepository
) {

    operator fun invoke(
        searchQuery: String
    ): Flow<UiState<NewsResponse>> = flow{
        try {
            emit(UiState.Loading())
            val newsList = repository.searchNews(searchQuery)
            emit(manageResponse(newsList))
        } catch (e: HttpException) {
            emit(UiState.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(UiState.Error("Couldn't reach server. Check you internet connection"))
        }
    }

    private fun manageResponse(newsListResponse: Response<NewsResponse>): UiState<NewsResponse> {
        if (newsListResponse.isSuccessful){
            newsListResponse.body()?.let { response ->
                return UiState.Success(response)
            }
        }
        return UiState.Error(newsListResponse.message())
    }

}