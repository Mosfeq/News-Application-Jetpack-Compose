package com.example.newsapplicationjetpackcompose.presentation.screens.saved_news_list

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.newsapplicationjetpackcompose.presentation.navigation.Screens
import com.example.newsapplicationjetpackcompose.presentation.screens.common.TTSManager
import com.example.newsapplicationjetpackcompose.presentation.screens.saved_news_list.components.SavedNewsListItem
import com.example.newsapplicationjetpackcompose.presentation.screens.saved_news_list.components.SwipeToDismissItem
import com.example.newsapplicationjetpackcompose.util.UiState
import java.net.URLEncoder

@Composable
fun SavedNewsListScreen(
    navController: NavController,
    viewModel: SavedNewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val savedNewsListState by viewModel.savedNewsListState.collectAsStateWithLifecycle()

    val ttsManager = remember { TTSManager(context) }

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
                text = "Saved Articles",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Your bookmarked news",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                when (savedNewsListState) {
                    is UiState.Success -> {
                        savedNewsListState.data?.let { listOfArticles ->
                            if (listOfArticles.isEmpty()) {
                                item {
                                    EmptySavedNewsView()
                                }
                            } else {
                                item {
                                    Text(
                                        text = "${listOfArticles.size} saved ${if (listOfArticles.size == 1) "article" else "articles"}",
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
                                    items = listOfArticles,
                                    key = { it.source?.uuid!! }
                                ) { article ->
                                    SwipeToDismissItem(
                                        item = article,
                                        onRemove = {
                                            article.title?.let {
                                                viewModel.removeNews(it)
                                            }
                                        }
                                    ) {
                                        SavedNewsListItem(
                                            article = article,
                                            onItemClick = {
                                                val encodedUrl =
                                                    URLEncoder.encode(article.url, "UTF-8")
                                                navController.navigate(Screens.NewsWebScreen.route + "/${encodedUrl}")
                                            },
                                            onT2SClick = {
                                                val textToRead = it.description ?: it.title
                                                ?: "No content available"
                                                val title = it.title ?: "News Article"
                                                ttsManager.startTextToSpeech(textToRead, title)
                                                Toast.makeText(
                                                    context,
                                                    "Starting text to speech...",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        )
                                    }
                                }
                                item {
                                    Text(
                                        text = "💡 Swipe left to remove articles",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 16.dp)
                                    )
                                }
                            }
                        }
                    }

                    is UiState.Error -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillParentMaxSize()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "⚠️",
                                        style = MaterialTheme.typography.displayMedium
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Something went wrong",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    savedNewsListState.errorMessage?.let {
                                        Text(
                                            text = it,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onBackground.copy(
                                                alpha = 0.6f
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is UiState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Loading saved articles...",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptySavedNewsView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "No saved articles",
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Saved Articles Yet",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bookmark articles you want to read later",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}
