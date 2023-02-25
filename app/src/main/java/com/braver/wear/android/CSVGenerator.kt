package com.braver.wear.android

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*

class CSVGenerator {
    fun exportDataToCsv(context: Context, timestamp: String, volume: String, location: String) {

        val mString = location.split(";").toTypedArray()
        val latitude = mString[0].toDouble()
        val longitude = mString[1].toDouble()
        var locationName = location

        val folder = File(context.filesDir, LOGS_FOLDER)
        if (folder.isDirectory) {
            val folders = folder.listFiles { file -> file.isDirectory }
            folders?.forEach {
                val folderLocationName = it.name.split(";").toTypedArray()
                val folderLatitude = folderLocationName[0].toDouble()
                val folderLongitude = folderLocationName[1].toDouble()

                if (latitude - folderLatitude < 0.0005 && latitude - folderLatitude > -0.0005) {
                    if (longitude - folderLongitude < 0.0005 && longitude - folderLongitude > -0.0005) {
                        locationName = it.name
                    }
                }
            }
        }

        val directoryPath: String = (Environment.getExternalStorageDirectory().path +
                LOGS_FOLDER + "/" + locationName)
        val bfile = File(directoryPath)

        if (!bfile.exists()) {
            bfile.mkdirs()
        }

        val dayShift = convertTimestamp(timestamp)
        val bfile2: File = File(bfile, "$dayShift.txt")

        if (!bfile2.exists()) {
            try {
                FileWriter(bfile2, false).use { fileWriter ->
                    fileWriter.append(volume + "\n")
                    LyaWearStatistics().createStatisticalData(bfile, bfile2)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            try {
                FileWriter(bfile2, true).use { fileWriter ->
                    fileWriter.append(volume + "\n")
                    LyaWearStatistics().createStatisticalData(bfile, bfile2)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun convertTimestamp(timestamp: String): String {
        return (timestamp.toInt() / 2).toString()
    }

    companion object {
        const val LOGS_FOLDER = "/Documents/LyaWear/Logs/"
    }
}