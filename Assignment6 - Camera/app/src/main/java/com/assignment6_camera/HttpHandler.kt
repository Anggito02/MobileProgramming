package com.assignment6_camera

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import java.util.Base64

class HttpHandler {
    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadImage(requestUrl : String, imageByteArray : ByteArray): Boolean {
        val imageBase64 : String = Base64.getEncoder().encodeToString(imageByteArray)
        val jsonObject = JSONObject()
        jsonObject.put("image", imageBase64)

        try {
            var flag : Boolean = false
            val url = URL(requestUrl)
            val conn : HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doInput = true
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")

            val outputStream = OutputStreamWriter(conn.outputStream)
            outputStream.write(jsonObject.toString())
            outputStream.flush()

            flag = conn.responseCode == HttpURLConnection.HTTP_OK
            conn.disconnect()
            return flag

        } catch (ex : MalformedURLException) {
            Log.e("HttpHandler", "MalformedURLException: " + ex.message)
            return false
        } catch (ex : ProtocolException) {
            Log.e("HttpHandler", "ProtocolException: " + ex.message)
            return false
        } catch (ex : IOException) {
            Log.e("HttpHandler", "IOException: " + ex.message)
            return false
        } catch (ex : Exception) {
            Log.e("HttpHandler", "Exception: " + ex.message)
            return false
        }
    }
}