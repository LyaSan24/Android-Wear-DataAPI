/*
 *  Created by https://github.com/braver-tool on 05/05/22, 12:10 PM
 *  Copyright (c) 2022 . All rights reserved.
 */

package com.braver.wear.android

import android.content.Context
import android.media.AudioManager
import android.util.Log
import android.widget.Toast
import com.braver.wear.android.WearActivity.Companion.EXTRA_METRIC_VOLUME
import com.braver.wear.android.WearActivity.Companion.PATH_FOR_WEAR
import com.google.android.gms.common.data.FreezableUtils
import com.google.android.gms.wearable.*

class MobileDataListenerService : WearableListenerService(), DataApi.DataListener {
    var TAG = "MobileDataListenerService"
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
                if (PATH_FOR_WEAR == path) {
                    val dataMapItem = DataMapItem.fromDataItem(event.dataItem)
                    val mVolumeMetric = dataMapItem.dataMap.getString(EXTRA_METRIC_VOLUME)
                    val volumeInt = mVolumeMetric?.toIntOrNull()
                    if (mVolumeMetric != null && volumeInt != null) {
                        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeInt, 0)
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
