package com.assignment4_todoapp

import android.content.Context
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader

class StorageHandler {
    fun saveData(context: Context, filename: String, data: String): Boolean {
        var fos: FileOutputStream? = null
        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE)
            fos.write(data.toByteArray())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            fos?.close()
        }
    }

    fun getData(context: Context, filename: String): String? {
        var fis: FileInputStream? = null
        var text = ""
        try {
            fis = context.openFileInput(filename)
            val isr = InputStreamReader(fis)
            val br = BufferedReader(isr)
            val sb = StringBuilder()
            var line: String? = br.readLine()
            while (line != null) {
                sb.append(line)
                sb.append("\n")
                line = br.readLine()
            }
            text = sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            fis?.close()
        }
        return text
    }
}