/*
 *  Created by https://github.com/braver-tool on 05/05/22, 12:10 PM
 *  Copyright (c) 2022 . All rights reserved.
 */

package com.braver.wear.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.braver.wear.android.MainActivity.Companion.EXTRA_MESSAGE_FROM_WEAR
import com.braver.wear.android.MainActivity.Companion.PATH_FOR_MOBILE
import com.google.android.gms.common.data.FreezableUtils
import com.google.android.gms.wearable.*

class WearDataListenerService : WearableListenerService(), DataApi.DataListener {
    var TAG = "WearListenerService"
    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        Log.i(
            "##BTApp-Wear@@$TAG",
            "--------->onDataChanged"
        )
        val events: List<DataEvent> =
            FreezableUtils.freezeIterable(dataEvents)
        dataEvents.close()
        for (event in events) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val path = event.dataItem.uri.path
                if (PATH_FOR_MOBILE == path) {
                    val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                    val wMessage = dataMapItem.dataMap.getString(EXTRA_MESSAGE_FROM_WEAR)

                    if (wMessage != null) {
                        val mString = wMessage.split("/").toTypedArray()
                        CSVGenerator().exportDataToCsv(this, mString[0], mString[1], mString[2])
                        Values.CURRENT_SHIFT = mString[2]
                        Values.CURRENT_VOLUME = mString[1]
                        Values.CURRENT_LOCATION = mString[2]
                    }
                } else {
                    Log.i(
                        "##BTApp-Wear@@$TAG",
                        "--------->path is none"
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Log.i(
            "##BTApp-Wear@@$TAG",
            "--------->onCreate"
        )



    }

    override fun onMessageReceived(p0: MessageEvent) {
        super.onMessageReceived(p0)
        Log.i(
            "##BTApp-Wear@@$TAG",
            "--------->onMessageReceived"
        )
    }
}
