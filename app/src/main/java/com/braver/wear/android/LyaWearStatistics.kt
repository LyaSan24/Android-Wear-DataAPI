package com.braver.wear.android

import java.io.*

class LyaWearStatistics {
    /*
        1. pegar a pasta localização e turno
        2. dentro dessas pastas, pegar o valor atual do volume
        3. trabalhar com média, mediana e moda
        4. pega a lista de dados coletados na pasta e gera os valores estatísticos
        5. cria um arquivo para cada informação
        6. os nomes vão ser "TURNO_average.txt", "TURNO_median.txt" e "TURNO_mode.txt"
    */
    fun createStatisticalData(locFile: File, file: File) {
        val lines = mutableListOf<Int>()
        val fileName = file.name
        if (file.exists()) {
            try {
                val reader = BufferedReader(FileReader(file))
                var line = reader.readLine()

                while (line != null) {
                    lines.add(line.toInt())
                    line = reader.readLine()
                }
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val averageFile: File = File(locFile, fileName + "_average.txt")
        val medianFile: File = File(locFile, fileName + "_median.txt")
        val modeFile: File = File(locFile, fileName + "_mode.txt")
        val linesFile: File = File(locFile, fileName + "_lines.txt")

        createAverageFile(averageFile, getAverage(lines))
        createMedianFile(medianFile, getMedian(lines))
        createModeFile(modeFile, getMode(lines))
        createLinesFile(linesFile, lines.size.toString())

    }

    private fun createLinesFile(linesFile: File, size: String) {
        try {
            FileWriter(linesFile, false).use { fileWriter ->
                fileWriter.append(size)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createModeFile(locFile: File, mode: String) {
        try {
            FileWriter(locFile, false).use { fileWriter ->
                fileWriter.append(mode)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createMedianFile(locFile: File, median: String) {
        try {
            FileWriter(locFile, false).use { fileWriter ->
                fileWriter.append(median)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun createAverageFile(locFile: File, average: String) {
        try {
            FileWriter(locFile, false).use { fileWriter ->
                fileWriter.append(average)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getAverage(m: List<Int>): String {
        var sum = 0.0
        for (i in m.indices) {
            sum += m[i].toDouble()
        }
        val mean = sum.toInt() / m.size
        return mean.toString()
    }

    private fun getMedian(m: List<Int>): String {
        val middle = m.size / 2
        return if (m.size % 2 == 1) {
            m[middle].toString()
        } else {
            val median = (m[middle - 1] + m[middle]) / 2
            median.toString()
        }
    }

    private fun getMode(listA: List<Int>): String {
        var maxValue = 0
        var maxCount = 0
        for (i in listA.indices) {
            var count = 0
            for (j in listA.indices) {
                if (listA[j] == listA[i]) ++count
            }
            if (count > maxCount) {
                maxCount = count
                maxValue = listA[i]
            }
        }
        return maxValue.toString()
    }
}