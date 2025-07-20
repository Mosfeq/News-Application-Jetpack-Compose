package com.example.newsapplicationjetpackcompose.presentation.screens.news_list

import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import java.util.Locale

@Composable
fun NewsListScreen(
    navController: NavController,
    viewModel: NewsListViewModel = hiltViewModel(),
    counterViewModel: CounterViewModel = hiltViewModel()
){

    val context = LocalContext.current

    val newsList by viewModel.newsList.collectAsStateWithLifecycle()

    val ttsManager = remember { TTSManager(context) }

//    val textToSpeech =  remember { mutableStateOf<TextToSpeech?>(null) }
//    var isTTSReady by remember { mutableStateOf(false) }

    var userInput: String by remember { mutableStateOf("") }
    var searchBtnClicked: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { ttsManager.bindService() }

    DisposableEffect(Unit) {
        onDispose {
            ttsManager.unbindService()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it},
                label = { Text("Search News 🗞️") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
            )
            Button(
                modifier = Modifier
                    .padding(0.dp, 20.dp, 0.dp, 15.dp),
                onClick = {
                    if (userInput.isNotBlank()){
                        viewModel.getNewsList(userInput)
                        searchBtnClicked = true
                        userInput = ""
                    } else {
                        Toast.makeText(context, "Type News to Search", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(0.dp, 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Find",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Icon(
                        Icons.Filled.Search,
                        "",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
            ) {
                when(newsList){
                    is UiState.Success -> {
                        newsList.data?.let { newsResponse ->
                            if (newsResponse.articles.isEmpty()){
                                Log.e("NewsListScreen", "$newsList : Main Screen (Empty List)")
                                item{
                                    Box(
                                        modifier = Modifier.fillParentMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text(
                                            text = "Search Something to output 🔍",
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                                if (searchBtnClicked){
                                    Toast.makeText(context, "No news published related to the search!", Toast.LENGTH_SHORT).show()
                                    searchBtnClicked = false
                                }
                            } else {
                                items(
                                    items = newsResponse.articles,
                                    key = { article ->
                                        article.source?.uuid!!
                                    }
                                ){ article ->
                                    if (article.title == "[Removed]" || article.urlToImage == null){
                                        Log.e("NewsListScreen", "[Removed] + no image == removed")
                                    } else{
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
                                                Toast.makeText(context, "Starting text to speech...",Toast.LENGTH_SHORT). show()
//                                                if (isTTSReady){
//                                                    textToSpeech.value?.speak(it.description, TextToSpeech.QUEUE_FLUSH, null)
//                                                } else {
//                                                    Toast.makeText(context, "Text-to-Speech is initializing...", Toast.LENGTH_SHORT).show()
//                                                }
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
    }

    LaunchedEffect(true) {
        viewModel.addNews.collectLatest {addNewsResponse ->
            when(addNewsResponse){
                is UiState.Success -> {
                    Toast.makeText(context, "${addNewsResponse.data}", Toast.LENGTH_SHORT).show()
                }
                is UiState.Error -> {
                    Toast.makeText(context, "${addNewsResponse.errorMessage}", Toast.LENGTH_SHORT).show()
                }
                is UiState.Loading -> {}
            }
        }
    }

//    LaunchedEffect(key1 = Unit) {
//        if (textToSpeech.value == null) {
//            textToSpeech.value = TextToSpeech(context) { status ->
//                if (status == TextToSpeech.SUCCESS) {
//                    textToSpeech.value?.language = Locale.getDefault()
//                    isTTSReady = true
//                }
//            }
//        }
//    }
//
//    DisposableEffect(key1 = Unit) {
//        onDispose {
//            textToSpeech.value?.stop()
//            textToSpeech.value?.shutdown()
//        }
//    }

}