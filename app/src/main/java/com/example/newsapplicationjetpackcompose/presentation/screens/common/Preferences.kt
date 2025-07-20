package com.example.newsapplicationjetpackcompose.presentation.screens.common

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("article_access_info")
    companion object {
        val readCounterKey = intPreferencesKey("readCounterValue")
        val savedCounterKey = intPreferencesKey("savedCounterValue")
    }

    val readCounter = context.dataStore.data
        .catch { exception ->
            Log.e("Preferences1", "Error reading readCounter", exception)
            emit(androidx.datastore.preferences.core.emptyPreferences())
        }
        .map { preferences ->
            val value = preferences[readCounterKey] ?: 0
            Log.d("Preferences1", "ReadCounter value: $value")
            value
        }

    val savedCounter = context.dataStore.data
        .catch { exception ->
            Log.e("Preferences1", "Error reading savedCounter", exception)
            emit(androidx.datastore.preferences.core.emptyPreferences())
        }
        .map { preferences ->
            val value = preferences[savedCounterKey] ?: 0
            Log.d("Preferences1", "SavedCounter value: $value")
            value
        }

    suspend fun increaseReadCounter(readCount: Int){
        try {
            Log.d("Preferences1", "Saving readCounter: $readCount")
            context.dataStore.edit { settings ->
                settings[readCounterKey] = readCount
            }
        } catch (e: Exception) {
            Log.e("Preferences1", "Error saving readCounter", e)
        }
    }

    suspend fun increaseSavedCounter(savedCount: Int){
        try {
            Log.d("Preferences1", "Saving savedCounter: $savedCount")
            context.dataStore.edit { settings ->
                settings[savedCounterKey] = savedCount
            }
        } catch (e: Exception) {
            Log.e("Preferences1", "Error saving savedCounter", e)
        }
    }

}