package com.assignment6_camera

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.text.format.DateFormat
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class MainActivityController(private val context: Context, private val callback: UpdateImageViewCallback) {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun saveImageFile(intent : Intent?) {
        try {
            if (intent == null) {
                throw Exception("Error passing image file!")
            }

            val imageBitmap : Bitmap = intent.getParcelableExtra("data", Bitmap::class.java)
                ?: throw Exception("Error passing image file!")

            val byteOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOutputStream)
            val byteArray : ByteArray = byteOutputStream.toByteArray()

            // save to external storage
            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString())
            val date = Date()
            val dateNow = DateFormat.format("MM-dd-yyyy hh-mm-ss", date.time)
            val imageUri = File(dir, "capture_${dateNow}")

            val fileOutputStream = FileOutputStream(imageUri)
            fileOutputStream.write(byteArray)
            fileOutputStream.flush()
            fileOutputStream.close()

            Toast.makeText(context, "Image has been saved to the Gallery", Toast.LENGTH_SHORT).show()

            // update the image view
            callback.updateImageView(imageBitmap)

        } catch (ex : Exception) {
            throw ex
        }
    }
}