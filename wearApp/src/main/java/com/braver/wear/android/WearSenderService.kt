package com.braver.wear.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import java.util.*

class WearSenderService : Service() {
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //Call thread
        val timer = Timer(false)
        val timerTask: TimerTask = object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun run() {
                WearActivity().sendRandomMessageToMobileApp()
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 10000)

        //Create notification
        val channelId = "Foreground Service ID"
        val channel = NotificationChannel(
            channelId,
            channelId,
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        val notification: Notification.Builder = Notification.Builder(this, channelId)
            .setContentText("LyaWear")
            .setContentTitle("Service in background")
            .setSmallIcon(R.mipmap.app_logo)
        startForeground(1001, notification.build())
        return super.onStartCommand(intent, flags, startId)
    }
}