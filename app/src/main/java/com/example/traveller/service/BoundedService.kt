package com.example.traveller.service


import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.os.HandlerCompat
import kotlin.concurrent.thread


class BoundedService : Service() {
    class Bound(val service: BoundedService) : Binder() // pośrednik między serwisem a aktywnością

    private val mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun startJob() {
        thread {
            for (i in 1..10) {
                mainHandler.post {
                    Toast.makeText(this, "Counting: $i", Toast.LENGTH_SHORT).show()
                }
                Thread.sleep(2000)
            }
            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = Bound(this).also { startJob() }
}