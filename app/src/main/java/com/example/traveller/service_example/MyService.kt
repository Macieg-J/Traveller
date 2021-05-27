package com.example.traveller.service_example

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService : Service() {

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int { // zostaje wykonana w momencie uruchomienia serwisu
        startJob()
        return START_STICKY
//        return super.onStartCommand(intent, flags, startId)
    }

    private fun startJob() {

    }

    //        TODO("Return the communication channel to the service.")
    override fun onBind(intent: Intent): IBinder? = null

}