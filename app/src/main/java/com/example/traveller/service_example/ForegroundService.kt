package com.example.traveller.service_example

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.os.HandlerCompat
import com.example.traveller.MainActivity
import com.example.traveller.NOTIFICATION_CHANNEL_DEFAULT
import com.example.traveller.R

const val FOREGROUND_NOTIFICATION_ID = 1

class ForegroundService : Service() { // ma notyfikacje ktorych nie da sie usunac

    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val defaultPendingIntentFlag = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_DEFAULT)
            .setContentTitle("Service Name")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .addAction(
                0,
                "",
                PendingIntent.getActivity(
                    this,
                    1,
                    Intent(this, MainActivity::class.java),
                    defaultPendingIntentFlag
                )
            ) // do dodawania akcji w notyfikacji
            .build()

        startForeground(FOREGROUND_NOTIFICATION_ID, notification)
        startJob()
        return START_NOT_STICKY
//        return super.onStartCommand(intent, flags, startId)
    }

    private fun startJob() {

    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}