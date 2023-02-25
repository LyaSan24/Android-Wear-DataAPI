package com.braver.wear.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.DataApi
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import java.util.*

class WearSenderService : Service() {

    private var mGoogleApiClient: GoogleApiClient? = null
    private val connectionCallbacks =
        this as com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks
    private val onConnectionFailed =
        this as com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailed)
                .build()
        }
        mGoogleApiClient!!.connect()

        //Call thread
        val timer = Timer(false)
        val timerTask: TimerTask = object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun run() {
                sendRandomMessageToMobileApp()
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

    fun sendRandomMessageToMobileApp() {
        val wearAvailable = mGoogleApiClient!!.hasConnectedApi(Wearable.API)
        Log.i("TAG", "----hasConnectedApi------->$wearAvailable")
        val dataMapRequest = PutDataMapRequest.create(WearActivity.PATH_FOR_MOBILE)
        val map = dataMapRequest.dataMap
        map.putString(
            WearActivity.EXTRA_MESSAGE_FROM_WEAR,
            getTimestampedDate() + "/" + getVolume(this) + "/" + LocationCollect().getCoordinatesTranslated(
                this
            )
        )
        map.putLong(WearActivity.EXTRA_CURRENT_TIME, Date().time)
        val putDataRequest = dataMapRequest.asPutDataRequest()
        putDataRequest.setUrgent()
        Wearable.DataApi.putDataItem(mGoogleApiClient!!, putDataRequest)
            .setResultCallback { dataItemResult: DataApi.DataItemResult ->
                if (dataItemResult.status.isSuccess) {
                    Log.i("TAG", "----sendRandomMessageToMobileApp------->Successfully!!")
                } else {
                    Log.i("TAG", "----sendRandomMessageToMobileApp------->Failed!!")
                }
            }
    }

    var cal: Calendar = Calendar.getInstance()
    var hours = cal[Calendar.HOUR_OF_DAY]

    private fun getTimestampedDate(): String {
        //val date = "$dayNumber-$monthNumber-$year"
        // time = getTime()
        //return "$date $time"
        return hours.toString()
    }

    private fun getVolume(context: Context?): String {
        val am = context?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        return currentVolume.toString()
    }
}