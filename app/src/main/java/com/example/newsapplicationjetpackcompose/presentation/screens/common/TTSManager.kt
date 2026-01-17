package com.example.newsapplicationjetpackcompose.presentation.screens.common

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.StateFlow
import android.Manifest
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.AudioManager
import android.util.Log

class TTSManager(private val context: Context) {

    private var ttsService: TTSService? = null
    private var bound = false
    private var ttsBroadcastReceiver: TTSBroadcastReceiver? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            val binder = service as TTSService.TTSBinder
            ttsService = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            ttsService = null
            bound = false
        }
    }

    fun bindService(){
        val intent = Intent(context, TTSService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        registerBroadcastReceiver()
    }

    fun unbindService(){
        if (bound){
            context.unbindService(serviceConnection)
            bound = false
        }
        unregisterBroadcastReceiver()
    }

    private fun registerBroadcastReceiver(){
        ttsBroadcastReceiver = TTSBroadcastReceiver(this)

        val hasPhonePermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED

        val intentFilter = if (hasPhonePermission) {
            ttsBroadcastReceiver!!.getIntentFilter()
        } else {
            IntentFilter().apply {
                addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
            }
        }

        try {
            ContextCompat.registerReceiver(
                context,
                ttsBroadcastReceiver,
                intentFilter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        } catch (e: Exception){
            Log.e("TTSManager", "Failed to register broadcast receiver")
        }
    }

    private fun unregisterBroadcastReceiver(){
        ttsBroadcastReceiver?.let { receiver ->
            try {
                context.unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
                Log.e("TTSManger", "Received unregistered already")
            }
            ttsBroadcastReceiver = null
        }
    }

    fun startTextToSpeech(text: String, title: String = "News Article"){
        val intent = Intent(context, TTSService::class.java).apply {
            putExtra(TTSService.EXTRA_TEXT, text)
            putExtra(TTSService.EXTRA_TITLE, title)
        }
        context.startService(intent)
    }

//    fun pauseReading(){
//        ttsService?.pauseReading()
//    }

//    fun resumeReading(){
//        ttsService?.resumeReading()
//    }

    fun stopReading(){
        ttsService?.stopReading()
    }

//    fun isReading(): Boolean = ttsService?.isCurrentReading() ?: false

//    fun isPaused(): Boolean = ttsService?.isCurrentPaused() ?: false

    fun getTTSState(): StateFlow<TTSService.TTSState>? = ttsService?.ttState

}