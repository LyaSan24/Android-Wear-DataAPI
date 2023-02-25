package com.braver.wear.android

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import java.io.*
import java.util.*

class AutomaticVerificationService : Service() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //Call thread
        val timer = Timer(false)
        val timerTask: TimerTask = object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun run() {
                checkAutomaticChanges()
            }
        }
        timer.scheduleAtFixedRate(timerTask, 0, 60000)

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

    /*
        1. Pega a localização armazenada
        2. Verifica a pasta com a localização detectada usando a mesma lógica do raio de distância
        3. Pega o turno atual armazenado
        4. "TURNO_average.txt", "TURNO_median", etc...
        5. Gera uma média aritmética entre todos os arquivos estatísticos (3) .txt
        6. Compara a média gerada com o volume atual
        7. Verifica se o contexto já possui um bom número de coletas antes
        7. Se os valores forem diferentes, notifica o watch com o volume a ser definido
     */
    private fun checkAutomaticChanges() {

        val mString = Values.CURRENT_LOCATION.split(";").toTypedArray()
        val latitude = mString[0].toDouble()
        val longitude = mString[1].toDouble()
        var locationName = Values.CURRENT_LOCATION

        val folder = File(filesDir, CSVGenerator.LOGS_FOLDER)
        if (folder.isDirectory) {
            val folders = folder.listFiles { file -> file.isDirectory }
            folders?.forEach {
                val folderLocationName = it.name.split(";").toTypedArray()
                val folderLatitude = folderLocationName[0].toDouble()
                val folderLongitude = folderLocationName[1].toDouble()

                if (latitude - folderLatitude < 0.0005 && latitude - folderLatitude > -0.0005) {
                    if (longitude - folderLongitude < 0.0005 && longitude - folderLongitude > -0.0005) {
                        locationName = it.name

                        val bfile2: File = File(folder, Values.CURRENT_SHIFT + ".txt")
                        if (bfile2.exists()) {
                            val averageFile = File(folder, Values.CURRENT_SHIFT + "_average.txt")
                            val medianFile = File(folder, Values.CURRENT_SHIFT + "_median.txt")
                            val linesFile = File(folder, Values.CURRENT_SHIFT + "_lines.txt")
                            val modeFile = File(folder, Values.CURRENT_SHIFT + "_mode.txt")

                            createAverageVolume(averageFile, medianFile, modeFile, linesFile)
                        }
                    }
                }
            }
        }
    }

    private fun createAverageVolume(averageFile: File, medianFile: File, modeFile: File, linesFile: File) {
        var average = 0
        var median = 0
        var mode = 0
        var lines = 0

        if (linesFile.exists()) {
            try {
                val reader = BufferedReader(FileReader(linesFile))
                var line = reader.readLine()
                lines = line.toInt()
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (averageFile.exists()) {
            try {
                val reader = BufferedReader(FileReader(averageFile))
                var line = reader.readLine()
                average = line.toInt()
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (medianFile.exists()) {
            try {
                val reader = BufferedReader(FileReader(medianFile))
                var line = reader.readLine()
                median = line.toInt()
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (modeFile.exists()) {
            try {
                val reader = BufferedReader(FileReader(modeFile))
                var line = reader.readLine()
                mode = line.toInt()
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val finalSum = (average + median + mode) / 3
        if (finalSum != Values.CURRENT_VOLUME.toInt()) {
            if (lines >= Values.COLLECT_PARAM) {
                //Notifica a alteração para o smartwatch
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}