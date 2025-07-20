package com.example.newsapplicationjetpackcompose.presentation.screens.common.viewmodel

import androidx.lifecycle.ViewModel
import com.example.newsapplicationjetpackcompose.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DailyNotificationViewModel @Inject constructor(
    private val useCases: UseCases
): ViewModel() {

    fun scheduleDailyNotification(){
        useCases.dailyNotification()
    }

}