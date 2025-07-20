package com.example.newsapplicationjetpackcompose.presentation.screens.saved_news_list

import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.newsapplicationjetpackcompose.presentation.navigation.Screens
import com.example.newsapplicationjetpackcompose.presentation.screens.common.TTSManager
import com.example.newsapplicationjetpackcompose.presentation.screens.saved_news_list.components.SavedNewsListItem
import com.example.newsapplicationjetpackcompose.presentation.screens.saved_news_list.components.SwipeToDismissItem
import com.example.newsapplicationjetpackcompose.util.UiState
import java.net.URLEncoder
import java.util.Locale

@Composable
fun SavedNewsListScreen(
    navController: NavController,
    viewModel: SavedNewsViewModel = hiltViewModel()
){
    val context = LocalContext.current

    val savedNewsListState by viewModel.savedNewsListState.collectAsStateWithLifecycle()

    val ttsManager = remember { TTSManager(context) }

//    val textToSpeech =  remember { mutableStateOf<TextToSpeech?>(null) }
//    var isTTSReady by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { ttsManager.bindService() }

    DisposableEffect(Unit) {
        onDispose {
            ttsManager.unbindService()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxSize()
        ){
            when(savedNewsListState){
                is UiState.Success -> {
                    savedNewsListState.data?.let { listOfArticles ->
                        items(
                            items = listOfArticles,
                            key = {
                                it.source?.uuid!!
                            }
                        ){ article ->
                            SwipeToDismissItem(
                                item = article,
                                onRemove = {
                                    article.title?.let {
                                        viewModel.removeNews(it)
                                    }
                                }
                            ){
                                SavedNewsListItem(
                                    article = article,
                                    onItemClick = {
                                        val encodedUrl = URLEncoder.encode(article.url, "UTF-8")
                                        navController.navigate(Screens.NewsWebScreen.route + "/${encodedUrl}")
                                    },
                                    onT2SClick = {
                                        val textToRead = it.description ?: it.title ?: "No content available"
                                        val title = it.title ?: "News Article"
                                        ttsManager.startTextToSpeech(textToRead, title)
                                        Toast.makeText(context, "Starting text to speech...",Toast.LENGTH_SHORT). show()
//                                        if (isTTSReady){
//                                            textToSpeech.value?.speak(it.description, TextToSpeech.QUEUE_FLUSH, null)
//                                        } else {
//                                            Toast.makeText(context, "Text-to-Speech is initializing...", Toast.LENGTH_SHORT).show()
//                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    item {
                        savedNewsListState.errorMessage?.let { 
                            Text(
                                text = it,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                    Toast.makeText(context, "${savedNewsListState.errorMessage}", Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxSize(),
                            contentAlignment = Alignment.Center
                        ){
                            CircularProgressIndicator()
                        }
                    }
                }
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