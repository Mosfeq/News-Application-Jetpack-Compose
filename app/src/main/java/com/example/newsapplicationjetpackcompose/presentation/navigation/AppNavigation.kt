package com.example.newsapplicationjetpackcompose.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapplicationjetpackcompose.presentation.screens.news_detail.NewsWebScreen
import com.example.newsapplicationjetpackcompose.presentation.screens.news_list.NewsListScreen
import com.example.newsapplicationjetpackcompose.presentation.screens.saved_news_list.SavedNewsListScreen
import com.example.newsapplicationjetpackcompose.presentation.screens.trend.TrendsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (currentRoute == Screens.NewsWebScreen.route  + "/{url}"){
                TopAppBar(
                    title = { Text(text = "News Article")},
                    navigationIcon = {
                        IconButton(onClick = {navController.popBackStack()}) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentRoute != Screens.NewsWebScreen.route + "/{url}"){
                NavigationBar(
                    tonalElevation = 1.dp
                ) {
                    listOfNavItems.forEach { navItem ->
                        NavigationBarItem(
                            label = {
                                Text(
                                    text = navItem.label,
                                    color = if (currentDestination?.hierarchy?.any { it.route == navItem.route } == true)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = navItem.icon,
                                    contentDescription = null,
                                    tint = if (currentDestination?.hierarchy?.any { it.route == navItem.route } == true)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                    modifier = Modifier.size(23.dp)
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == navItem.route } == true,
                            onClick = {
                                navController.navigate(navItem.route){
                                    popUpTo(navController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.NewsListScreen.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Screens.NewsListScreen.route){
                NewsListScreen(navController = navController)
            }
            composable(
                route = Screens.NewsWebScreen.route + "/{url}",
                arguments = listOf(
                    navArgument("url"){
                        type = NavType.StringType
                        nullable = true
                    }
                )
            ){ entry ->
                val encodedUrl = entry.arguments?.getString("url")
                val decodedUrl = encodedUrl?.let { java.net.URLDecoder.decode(it, "UTF-8") }
                decodedUrl?.let { url -> NewsWebScreen(url = url) }
            }
            composable(route = Screens.SavedNewsListScreen.route){
                SavedNewsListScreen(navController = navController)
            }
            composable(route = Screens.TrendsScreen.route){
                TrendsScreen()
            }
        }
    }
}