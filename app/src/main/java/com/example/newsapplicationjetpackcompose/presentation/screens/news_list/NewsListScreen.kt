package com.example.newsapplicationjetpackcompose.presentation.screens.news_list

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.newsapplicationjetpackcompose.presentation.navigation.Screens
import com.example.newsapplicationjetpackcompose.presentation.screens.common.ShimmerItem
import com.example.newsapplicationjetpackcompose.presentation.screens.common.TTSManager
import com.example.newsapplicationjetpackcompose.presentation.screens.common.viewmodel.CounterViewModel
import com.example.newsapplicationjetpackcompose.presentation.screens.news_list.components.NewsListItem
import com.example.newsapplicationjetpackcompose.util.UiState
import kotlinx.coroutines.flow.collectLatest
import java.net.URLEncoder

@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsListViewModel = hiltViewModel(),
    counterViewModel: CounterViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val newsList by viewModel.newsList.collectAsStateWithLifecycle()

    val ttsManager = remember { TTSManager(context) }

    var userInput: String by remember { mutableStateOf("") }
    var hasSearched: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { ttsManager.bindService() }

    DisposableEffect(Unit) {
        onDispose {
            ttsManager.unbindService()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Discover News",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Search for the latest articles",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Search News 🗞️") },
                placeholder = { Text("Type keywords...") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    if (userInput.isNotBlank()) {
                        viewModel.getNewsList(userInput)
                        hasSearched = true
                        userInput = ""
                    } else {
                        Toast.makeText(context, "Type News to Search", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 6.dp
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Search Articles",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            when (newsList) {
                is UiState.Success -> {
                    newsList.data?.let { newsResponse ->
                        if (newsResponse.articles.isEmpty()){
                            Log.e("NewsListScreen", "$newsList : Main Screen (Empty List)")
                            item {
                                EmptyStateView()
                            }
                            if (hasSearched) {
                                Toast.makeText(context, "No news published related to the search!", Toast.LENGTH_SHORT).show()
                                hasSearched = false
                            }
                        } else {
                            item {
                                Text(
                                    text = "${newsResponse.articles.size} articles found",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(
                                        horizontal = 20.dp,
                                        vertical = 8.dp
                                    )
                                )
                            }

                            items(
                                items = newsResponse.articles,
                                key = { article -> article.source?.uuid!! }
                            ){ article ->
                                if (article.title == "[Removed]" || article.urlToImage == null) {
                                    Log.e("NewsListScreen", "[Removed] + no image == removed")
                                } else {
                                    NewsListItem(
                                        article = article,
                                        onItemClick = {
                                            counterViewModel.increaseReadCounter()
                                            val encodedUrl =
                                                URLEncoder.encode(article.url, "UTF-8")
                                                navController.navigate(Screens.NewsWebScreen.route + "/${encodedUrl}")
                                        },
                                        onT2SClick = {
                                            val textToRead = it.description ?: it.title ?: "No content available"
                                            val title = it.title ?: "News Article"
                                            ttsManager.startTextToSpeech(textToRead, title)
                                            Toast.makeText(context, "Starting text to speech...", Toast.LENGTH_SHORT).show()
                                        },
                                        onSaveClick = {
                                            counterViewModel.increaseSavedCounter()
                                            viewModel.addNews(it)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Toast.makeText(context, newsList.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {
                    Log.e("NewsListScreen", "$newsList : Main Screen")
                    items(5) {
                        ShimmerItem()
                    }
                }
            }
        }
    }

    LaunchedEffect(true) {
        viewModel.addNews.collectLatest { addNewsResponse ->
            when (addNewsResponse) {
                is UiState.Success -> {
                    Toast.makeText(context, "${addNewsResponse.data}", Toast.LENGTH_SHORT).show()
                }

                is UiState.Error -> {
                    Toast.makeText(context, "${addNewsResponse.errorMessage}", Toast.LENGTH_SHORT)
                        .show()
                }

                is UiState.Loading -> {}
            }
        }
    }
}

@Composable
private fun EmptyStateView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "No results",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Start Your Search",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Search for news articles using keywords",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }
    }
}