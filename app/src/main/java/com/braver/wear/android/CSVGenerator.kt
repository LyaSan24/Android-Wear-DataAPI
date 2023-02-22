package com.braver.wear.android

import android.content.Context
import android.os.Environment
import android.text.format.DateFormat
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class CSVGenerator {
    fun exportDataToCsv(context: Context, timestamp: String, volume: String, location: String) {

        val directoryPath: String = (Environment.getExternalStorageDirectory().path +
                LOGS_FOLDER + "/" + location)
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
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            try {
                FileWriter(bfile2, false).use { fileWriter ->
                    fileWriter.append(volume + "\n")
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