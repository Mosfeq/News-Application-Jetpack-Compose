package com.example.newsapplicationjetpackcompose.domain.use_case

import com.example.newsapplicationjetpackcompose.domain.repository.NewsRepository
import javax.inject.Inject

class DailyNotificationUseCase @Inject constructor(
    private val repository: NewsRepository
){
    operator fun invoke(){
        repository.scheduleDailyNotifications()
    }
}