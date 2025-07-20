package com.example.newsapplicationjetpackcompose.presentation.screens.saved_news_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapplicationjetpackcompose.domain.model.Article
import com.example.newsapplicationjetpackcompose.domain.use_case.UseCases
import com.example.newsapplicationjetpackcompose.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedNewsViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {

    private val _savedNewsListState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading())
    val savedNewsListState = _savedNewsListState.asStateFlow()

    init {
        getSavedNewsList()
    }

    private fun getSavedNewsList(){
        useCases.getSavedNews{ response ->
            viewModelScope.launch {
                when(response){
                    is UiState.Success -> {
                        response.data?.let {
                            _savedNewsListState.emit(UiState.Success(it))
                        }
                    }
                    is UiState.Error -> {
                        response.errorMessage?.let {
                            _savedNewsListState.emit(UiState.Error(it))
                        }
                    }
                    is UiState.Loading -> {
                        _savedNewsListState.emit(UiState.Loading())
                    }
                }
            }
        }
    }

    fun removeNews(newsTitle: String){
        useCases.removeNews(newsTitle)
    }

}