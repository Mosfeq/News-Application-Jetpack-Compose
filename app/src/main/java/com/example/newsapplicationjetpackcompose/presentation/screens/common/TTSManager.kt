package com.example.newsapplicationjetpackcompose.presentation.screens.common

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import kotlinx.coroutines.flow.StateFlow

class TTSManager(private val context: Context) {

    private var ttsService: TTSService? = null
    private var bound = false

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
    }

    fun unbindService(){
        if (bound){
            context.unbindService(serviceConnection)
            bound = false
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