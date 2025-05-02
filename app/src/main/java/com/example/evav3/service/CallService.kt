package com.example.evav3.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import com.example.evav3.R
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class CallService : Service() {

    companion object {
        const val CHANNEL_ID       = "call_foreground"
        const val NOTIF_ID         = 1001
        const val ACTION_END_CALL  = "com.example.evav3.ACTION_END_CALL"
        const val EXTRA_SESSION_ID = "extra_session_id"
    }

    private val binder = LocalBinder()
    private var isMuted = false
    private var startTime: Long = 0L
    private lateinit var notificationManager: NotificationManager
    private lateinit var notification: Notification
    private var timer: Timer? = null
    private var sessionId: String? = null

    inner class LocalBinder : Binder() {
        fun getService(): CallService = this@CallService
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createChannel()
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_END_CALL -> {
                stopSelf()
                return START_NOT_STICKY
            }
            else -> {
                // Starting call
                sessionId = intent?.getStringExtra(EXTRA_SESSION_ID)
                startCall()
            }
        }
        return START_STICKY
    }

    private fun startCall() {
        startTime = SystemClock.elapsedRealtime()
        PrefsHelper.setCallActive(this, true)
        sessionId?.let { PrefsHelper.setActiveSession(this, it) }

        // build persistent notification
        val endIntent = Intent(this, CallService::class.java).apply {
            action = ACTION_END_CALL
        }
        val endPending = PendingIntent.getService(
            this, 0, endIntent, PendingIntent.FLAG_IMMUTABLE
        )

        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_call)
            .setContentTitle("EVA Call Active")
            .setContentText("00:00")
            .setOngoing(true)
            .addAction(R.drawable.ic_end_call, "End", endPending)
            .build()

        startForeground(NOTIF_ID, notification)
        startTimer()

        // TODO: open streaming API, write transcript to Firestore:
        // val db = FirebaseFirestore.getInstance()
    }

    private fun startTimer() {
        timer = Timer().apply {
            scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val elapsed = SystemClock.elapsedRealtime() - startTime
                    val minutes = (elapsed / 1000) / 60
                    val seconds = (elapsed / 1000) % 60
                    val text = String.format("%02d:%02d", minutes, seconds)
                    notification = NotificationCompat.Builder(this@CallService, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_call)
                        .setContentTitle("EVA Call Active")
                        .setContentText(text)
                        .setOngoing(true)
                        .build()
                    notificationManager.notify(NOTIF_ID, notification)
                }
            }, 1000, 1000)
        }
    }

    fun toggleMute(): Boolean {
        isMuted = !isMuted
        // TODO: actually mute/unmute microphone stream
        return isMuted
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        PrefsHelper.setCallActive(this, false)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                CHANNEL_ID,
                "EVA Call",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(chan)
        }
    }
}