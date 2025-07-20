package com.example.newsapplicationjetpackcompose.presentation.screens.common.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapplicationjetpackcompose.presentation.screens.common.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val preferences: Preferences
): ViewModel() {

    private val _readCounter = MutableStateFlow(0)
    val readCounter: StateFlow<Int>
        get() = _readCounter.asStateFlow()

    private val _savedCounter = MutableStateFlow(0)
    val savedCounter: StateFlow<Int>
        get() = _savedCounter.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                preferences.readCounter
                    .catch { exception ->
                        _readCounter.value = 0
                    }
                    .collect { value ->
                        _readCounter.value = value
                    }
            } catch (e: Exception) {
                Log.e("CounterViewModel", "Error in readCounter collection", e)
            }
        }

        viewModelScope.launch {
            try {
                preferences.savedCounter
                    .catch { exception ->
                        _savedCounter.value = 0
                    }
                    .collect { value ->
                        _savedCounter.value = value
                    }
            } catch (e: Exception) {
                Log.e("CounterViewModel", "Error in savedCounter collection", e)
            }
        }
    }

    fun increaseReadCounter(){
        viewModelScope.launch {
            try {
                val currentReadValue = _readCounter.value
                val newReadValue = currentReadValue + 1
                preferences.increaseReadCounter(newReadValue)
            } catch (e: Exception) {
                Log.e("CounterViewModel", "Error increasing readCounter", e)
            }
        }
    }

    fun increaseSavedCounter(){
        viewModelScope.launch {
            try {
                val currentSavedValue = _savedCounter.value
                val newSavedValue = currentSavedValue + 1
                preferences.increaseSavedCounter(newSavedValue)
            } catch (e: Exception) {
                Log.e("CounterViewModel", "Error increasing savedCounter", e)
            }
        }
    }

}