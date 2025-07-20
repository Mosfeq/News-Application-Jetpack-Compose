package com.example.newsapplicationjetpackcompose.data.remote.dao

import android.util.Log
import androidx.compose.runtime.key
import com.example.newsapplicationjetpackcompose.domain.model.Article
import com.example.newsapplicationjetpackcompose.util.UiState
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseDao {

    var newsItem: Article? = null
    private val database = FirebaseDatabase.getInstance("https://news-application-5189e-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("Saved News")

    private val keywordDatabase = FirebaseDatabase.getInstance("https://news-application-5189e-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("Searched words")


    fun getSavedNewsList(result: (UiState<List<Article>>) -> Unit){
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newsList = arrayListOf<Article>()
                for (document in snapshot.children){
                    newsItem = document.getValue(Article::class.java)
                    if (newsItem != null){
                        newsList.add(newsItem!!)
                    }
                }
                result.invoke(
                    UiState.Success(newsList)
                )
            }
            override fun onCancelled(error: DatabaseError) {
                result.invoke(
                    UiState.Error("Cannot Retrieve News")
                )
            }
        })
    }

    fun addNews(news: Article, response: (UiState<String>) -> Unit){
        news.title?.let {
            database.child(it).setValue(news)
                .addOnSuccessListener {
                    response.invoke(
                        UiState.Success("News Saved")
                    )
                }
                .addOnFailureListener{ error ->
                    error.localizedMessage?.let { errorMessage ->
                        response.invoke(
                            UiState.Error(errorMessage)
                        )
                    }
                }
        }
    }

    fun removeNews(newsTitle: String){
        database.child(newsTitle).removeValue()
    }

}