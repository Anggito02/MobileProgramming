package com.assignment5_jsonparser

import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL

class HttpHandler {
    fun makeServiceCall(requestUrl : String) : String {
        val response : String

        try {
            val url = URL(requestUrl)
            val conn : HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"

            val inputStream : InputStream = BufferedInputStream(conn.inputStream)
            response = _convertStreamToString(inputStream)
            return response
        } catch (ex : MalformedURLException) {
            Log.e("HttpHandler", "MalformedURLException: " + ex.message)
        } catch (ex : ProtocolException) {
            Log.e("HttpHandler", "ProtocolException: " + ex.message)
        } catch (ex : IOException) {
            Log.e("HttpHandler", "IOException: " + ex.message)
        } catch (ex : Exception) {
            Log.e("HttpHandler", "Exception: " + ex.message)
        }

        return ""
    }

    private fun _convertStreamToString(inputStream: InputStream) : String {
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        val strBuilder = StringBuilder()

        bufferReader.use { reader ->
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    strBuilder.append("$line\n")
                }
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }

        return strBuilder.toString()
    }
}