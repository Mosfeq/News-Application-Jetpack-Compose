package com.example.newsapplicationjetpackcompose.presentation.screens.common

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.core.app.NotificationCompat
import com.example.newsapplicationjetpackcompose.R
import com.example.newsapplicationjetpackcompose.presentation.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TTSService: Service(), TextToSpeech.OnInitListener {

    companion object{
//        const val ACTION_PLAY_PAUSE = "ACTION_PLAY_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
        const val NOTIFICATION_ID = 1001
        const val CHANNEL_ID = "TTS_CHANNEL"
        const val EXTRA_TEXT = "EXTRA_TEXT"
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }

    private val binder = TTSBinder()
    private var textToSpeech: TextToSpeech? = null
    private var notificationManager: NotificationManager? = null

    private var currentText = ""
    private var currentTitle = ""
//    private var isPaused = false
    private var isReading = false
    private var isTTSReady = false

    enum class TTSState {
        IDLE,
        READING,
//        PAUSED
    }
    private val _ttsState = MutableStateFlow(TTSState.IDLE)
    val ttState: StateFlow<TTSState> = _ttsState.asStateFlow()

    inner class TTSBinder: Binder(){
        fun getService(): TTSService = this@TTSService
    }

    override fun onCreate() {
        super.onCreate()
        initialiseTTS()
        createNotificationChannel()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action){
//            ACTION_PLAY_PAUSE -> togglePlayPause()
            ACTION_STOP -> stopTTS()
            else -> {
                intent?.let {
                    currentText = it.getStringExtra(EXTRA_TEXT) ?: ""
                    currentTitle = it.getStringExtra(EXTRA_TITLE) ?: "News Article"
                    if (currentText.isNotEmpty()){
                        startReading()
                    }
                }
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            textToSpeech?.language = Locale.getDefault()
            isTTSReady = true

            textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _ttsState.value = TTSState.READING
                    isReading = true
//                    isPaused = false
                    updateNotification()
                }

                override fun onDone(utteranceId: String?) {
                    _ttsState.value = TTSState.IDLE
                    isReading = false
//                    isPaused = false
                    stopForeground(true)
                }

                override fun onError(utteranceId: String?) {
                    _ttsState.value = TTSState.IDLE
                    isReading = false
//                    isPaused = false
                    stopForeground(true)
                }
            })
        }
    }

    private fun initialiseTTS(){
        textToSpeech = TextToSpeech(this, this)
    }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Text to Speech",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Text to Speech Notification"
                setShowBadge(false)
                setSound(null, null)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun startReading(){
        if (!isTTSReady || currentText.isEmpty()) return

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "tts_utterance")
        }

        textToSpeech?.speak(currentText, TextToSpeech.QUEUE_FLUSH, params, "tts_utterance")
        startForeground(NOTIFICATION_ID, createNotification())
    }

//    private fun togglePlayPause(){
//        if (!isTTSReady) return
//
//        when {
//            isReading && !isPaused -> {
//                textToSpeech?.stop()
//                isPaused = true
//                isReading = false
//                _ttsState.value = TTSState.PAUSED
//                updateNotification()
//            }
//            isPaused -> {
//                startReading()
//            }
//            else -> {
//                if (currentText.isNotEmpty()){
//                    startReading()
//                }
//            }
//        }
//    }

    private fun stopTTS(){
        textToSpeech?.stop()
        isReading = false
//        isPaused = false
        _ttsState.value  = TTSState.IDLE
        stopForeground(true)
    }

    private fun createNotification(): Notification {
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            this, 1, openAppIntent, PendingIntent.FLAG_IMMUTABLE
        )

//        val playPauseIntent = Intent(this, TTSService::class.java).apply {
//            action = ACTION_PLAY_PAUSE
//        }
//        val playPausePendingIntent = PendingIntent.getService(
//            this, 2, playPauseIntent, PendingIntent.FLAG_IMMUTABLE
//        )

        val stopIntent = Intent(this, TTSService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 3, stopIntent, PendingIntent.FLAG_IMMUTABLE
        )

//        val playPauseIcon = when {
//            isReading && !isPaused -> R.drawable.ic_pause
//            else -> R.drawable.ic_play
//        }
//
//        val playPauseText = when {
//            isReading && !isPaused -> "Pause"
//            isPaused -> "Resume"
//            else -> "Play"
//        }

        val statusText = when {
            isReading
//                    && !isPaused
                        -> "Reading..."
//            isPaused -> "Paused"
            else -> "Ready"
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Text to Speech: $currentTitle")
            .setContentText(statusText)
            .setSmallIcon(R.drawable.ic_play)
            .setContentIntent(openAppPendingIntent)
//            .addAction(playPauseIcon, playPauseText, playPausePendingIntent)
            .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification(){
        if (isReading
//            || isPaused
            ){
            notificationManager?.notify(NOTIFICATION_ID, createNotification())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }

    fun speakText(text: String, title: String = "News Article"){
        currentText = text
        currentTitle = title
        startReading()
    }

//    fun pauseReading(){
//        if (isReading && !isPaused){
//            togglePlayPause()
//        }
//    }

//    fun resumeReading(){
//        if (isPaused){
//            togglePlayPause()
//        }
//    }

    fun stopReading(){
        stopTTS()
    }

//    fun isCurrentReading(): Boolean = isReading && !isPaused
//    fun isCurrentPaused(): Boolean = isPaused
}