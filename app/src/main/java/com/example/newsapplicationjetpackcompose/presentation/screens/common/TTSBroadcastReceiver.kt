package com.example.newsapplicationjetpackcompose.presentation.screens.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.telephony.TelephonyManager

class TTSBroadcastReceiver(
    private val ttsManager: TTSManager
): BroadcastReceiver() {

    companion object {
        private const val TAG = "TTSBroadcastReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent) {
        when (intent?.action){
            TelephonyManager.ACTION_PHONE_STATE_CHANGED -> {
                handlePhoneStateChanges(intent)
            }

            AudioManager.ACTION_AUDIO_BECOMING_NOISY -> {
                ttsManager.stopReading()
            }
        }
    }

    private fun handlePhoneStateChanges(intent: Intent){
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        when (state) {
            TelephonyManager.EXTRA_STATE_RINGING -> {
                ttsManager.stopReading()
            }
            TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                ttsManager.stopReading()
            }
            TelephonyManager.EXTRA_STATE_IDLE -> {

            }
        }
    }

    fun getIntentFilter(): IntentFilter {
        return IntentFilter().apply {
            addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
            addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        }
    }

}