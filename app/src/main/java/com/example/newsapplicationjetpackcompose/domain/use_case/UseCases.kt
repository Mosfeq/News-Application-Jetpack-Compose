package com.example.newsapplicationjetpackcompose.domain.use_case

import javax.inject.Inject

data class UseCases @Inject constructor(
    val addNews: AddNewsUseCase,
    val getSavedNews: GetSavedNewsUseCase,
    val getNewsList: GetNewsListUseCase,
    val removeNews: RemoveNewsUseCase,
    val dailyNotification: DailyNotificationUseCase
)