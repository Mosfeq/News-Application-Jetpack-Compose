package com.example.newsapplicationjetpackcompose.presentation.navigation

sealed class Screens(val route: String) {
    object NewsListScreen: Screens("news_list_screen")
    object NewsWebScreen: Screens("news_web_screen")
    object SavedNewsListScreen: Screens("saved_news_list_screen")

    object TrendsScreen: Screens("trends_screen")
}