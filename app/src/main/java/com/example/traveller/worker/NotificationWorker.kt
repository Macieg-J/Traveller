package com.example.traveller.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.traveller.R
import com.example.traveller.database.Entry

private const val TAG = "NotificationWorker"

class NotificationWorker(
    val context: Context,
    workerParameters: WorkerParameters,
    private val listOfEntries: List<Entry> // fixme wyciagac liste z bazy
) :
    CoroutineWorker(context, workerParameters) {
    private val notificationId = 1
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as
            NotificationManager
    private val locationManager by lazy {
        getSystemService(
            context,
            LocationManager::class.java
        )
    }

    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "doWork() lunched!")
            val progress = "Starting searching for locations where you already have been to"
            setForeground(createForegroundInfo(progress))
            setProximityConfig()
            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }

    }

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        Log.d(TAG, "createForegroundInfo() lunched!")
        val id =
            applicationContext.getString(R.string.notification_channel_id) // fixme application context or just context?
        val title = applicationContext.getString(R.string.notification_title)
        val cancel = applicationContext.getString(R.string.notification_cancel)

        val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(getId())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel("Location_verifier_channel", "Location_Verifier")
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(android.R.drawable.ic_dialog_map) // fixme download and change to another one
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        context.getSystemService(NotificationManager::class.java).notify(1, notification) // to i createChannel do stworzonej klasy broadcast
        return ForegroundInfo(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(
        channelId: String,
        name: String
    ): NotificationChannel {
        Log.d(TAG, "createChannel() lunched!")
        return NotificationChannel(
            channelId,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setProximityConfig() {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            Intent(context, BroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        // todo create class that extends BroadcastReciver, add to manifest and implement onReceive method - next step is adding notification (notification
        listOfEntries.forEach { entry ->
            locationManager?.addProximityAlert(
                entry.latitude,
                entry.longitude,
                1000f,
                -1,
                pendingIntent
            )
        }
    }
}