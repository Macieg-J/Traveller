package com.example.traveller.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import com.example.traveller.MainActivity
import com.example.traveller.R
import com.example.traveller.camera.localisation.LocationModel
import kotlinx.parcelize.Parcelize

val channelId = "NOTIFICATION_CHANNEL"
val notificationId = 123

class MyBroadcastReceiver(
    val context: Context
) : BroadcastReceiver() {
    private val tag = "MyBroadcastReciver"
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as
            NotificationManager
    private lateinit var locations: List<LocationModel>

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(tag, "onReceive() launched!!!")
        val id =
            context!!.getString(R.string.notification_channel_id) // wrong context?
        val title = context.getString(R.string.notification_title)
        val cancel = context.getString(R.string.notification_cancel)
        val progress = "Starting searching for locations where you already have been to"

        createChannel("Location_verifier_channel", "Location_Verifier")

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(android.R.drawable.ic_dialog_map) // fixme download and change to another one
            .setOngoing(true)
//            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()
        Log.d(tag, "onReceive() notification: $notification")

        context.getSystemService(NotificationManager::class.java)
            .notify(notificationId, notification)

        if (intent != null) {
            if ("locationCheck" == intent.action) {
                var received = intent.getStringExtra("locationCheck")
                Toast.makeText(context, received, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createChannel(
        channelId: String,
        name: String
    ): NotificationChannel {
        Log.d(tag, "createChannel() lunched!")
        return NotificationChannel(
            channelId,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        ).also { channel ->
            notificationManager.createNotificationChannel(channel)
        }
    }

//    private class Task(
//        private val pendingResult: PendingResult,
//        private val intent: Intent
//    ) : AsyncTask<String, Int, String>() {
//        private val tag = "Task"
//
//        override fun doInBackground(vararg params: String?): String {
//            val sb = StringBuilder()
//            sb.append("Action: ${intent.action}\n")
//            sb.append("URI: ${intent.toUri(Intent.URI_INTENT_SCHEME)}\n")
//            return toString().also { log ->
//                Log.d(tag, log)
//            }
//        }
//
//        override fun onPostExecute(result: String?) {
//            super.onPostExecute(result)
//            // Must call finish() so the BroadcastReceiver can be recycled.
//            pendingResult.finish()
//        }
//    }
}