package com.example.newsapplicationjetpackcompose.data.repository

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.newsapplicationjetpackcompose.data.remote.api.NewsApi
import com.example.newsapplicationjetpackcompose.data.remote.dao.FirebaseDao
import com.example.newsapplicationjetpackcompose.data.worker.DailyNotificationWorker
import com.example.newsapplicationjetpackcompose.domain.model.Article
import com.example.newsapplicationjetpackcompose.domain.model.NewsResponse
import com.example.newsapplicationjetpackcompose.domain.repository.NewsRepository
import com.example.newsapplicationjetpackcompose.util.UiState
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsRepositoryImplementation @Inject constructor(
    private val api: NewsApi,
    private val db: FirebaseDao,
    private val context: Context
): NewsRepository {

    override suspend fun searchNews(searchQuery: String): Response<NewsResponse>{
        return api.searchNews(
            searchQuery = searchQuery
        )
    }

    override fun addNews(news: Article, response: (UiState<String>) -> Unit) {
        db.addNews(news){
            response.invoke(it)
        }
    }

    override fun getSavedNews(result: (UiState<List<Article>>) -> Unit) {
        db.getSavedNewsList {
            result.invoke(it)
        }
    }

    override fun removeNews(newsTitle: String) {
        db.removeNews(newsTitle)
    }

    override fun scheduleDailyNotifications() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(true)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<DailyNotificationWorker>(
            repeatInterval = 24,
            repeatIntervalTimeUnit = TimeUnit.HOURS
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_notification",
            ExistingPeriodicWorkPolicy.KEEP,
            dailyWorkRequest
        )
    }

}