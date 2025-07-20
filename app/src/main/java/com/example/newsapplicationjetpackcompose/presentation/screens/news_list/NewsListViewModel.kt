package com.example.newsapplicationjetpackcompose.presentation.screens.news_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapplicationjetpackcompose.domain.model.Article
import com.example.newsapplicationjetpackcompose.domain.model.NewsResponse
import com.example.newsapplicationjetpackcompose.domain.use_case.UseCases
import com.example.newsapplicationjetpackcompose.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsListViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {

    private var mutableNewsList = MutableStateFlow<UiState<NewsResponse>>(
        UiState.Success(NewsResponse(emptyList(), "", 0))
    )
    val newsList = mutableNewsList.asStateFlow()

    private var mutableAddNews = MutableSharedFlow<UiState<String>>()
    val addNews = mutableAddNews.asSharedFlow()

    fun getNewsList(userInput: String){
        useCases.getNewsList(
            searchQuery = userInput
        ).onEach { newsResponse ->
            when (newsResponse){
                is UiState.Success ->{
                    mutableNewsList.emit(newsResponse)
                    Log.e("NewsListScreen", "${mutableNewsList.value}")
                }
                is UiState.Error -> {
                    mutableNewsList.emit(newsResponse)
                }
                is UiState.Loading -> {
                    mutableNewsList.emit(newsResponse)
                    Log.e("NewsListScreen", "${mutableNewsList.value}")
                }
            }
        }.launchIn(viewModelScope)
    }

    fun addNews(news: Article){
        useCases.addNews(news){ response ->
            viewModelScope.launch(Dispatchers.IO) {
                Log.e("NewsListScreen", "$response")
                mutableAddNews.emit(response)
            }
        }
    }

}