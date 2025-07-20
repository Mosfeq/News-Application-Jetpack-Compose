package com.example.newsapplicationjetpackcompose.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItems(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val listOfNavItems = listOf(
    NavItems(
        label = "Search",
        icon = Icons.Default.Search,
        route = Screens.NewsListScreen.route
    ),
    NavItems(
        label = "Favourites",
        icon = Icons.Default.Favorite,
        route = Screens.SavedNewsListScreen.route
    ),
    NavItems(
        label = "Trends",
        icon = Icons.Default.Info,
        route = Screens.TrendsScreen.route
    )
)