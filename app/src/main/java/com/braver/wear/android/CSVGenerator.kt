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
        val date = Date()
        val cal = Calendar.getInstance()
        cal.time = date

        val day2 = DateFormat.format("dd", date) as String
        val monthNumber = DateFormat.format("MM", date) as String
        val year = DateFormat.format("yyyy", date) as String
        val directoryPath: String = (Environment.getExternalStorageDirectory().path + DYNAMIC_FOLDER)

        val bfile = File(directoryPath)
        if (!bfile.exists()) {
            bfile.mkdirs()
        }

        val bfile2: File = File(bfile, "$year$monthNumber$day2.csv")

        if (!bfile2.exists()) {
            var writer: CSVWriter? = null
            try {
                writer = CSVWriter(FileWriter(bfile2, true), ',', CSVWriter.NO_QUOTE_CHARACTER)
                val data: MutableList<Array<String>> = ArrayList()
                data.add(
                    arrayOf(
                        "timestamp", "volume", "location"
                    )
                )
                writer.writeAll(data)
                writer.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            var writer: CSVWriter? = null
            try {
                writer = CSVWriter(FileWriter(bfile2, true), ',', CSVWriter.NO_QUOTE_CHARACTER)
                val data: MutableList<Array<String>> = ArrayList()
                data.add(
                    arrayOf(
                        timestamp, volume, location
                    )
                )
                writer.writeAll(data)
                writer.close()
                //System.runFinalization();
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val DYNAMIC_FOLDER = "/documents/LyaWear/Logs/"
    }
}