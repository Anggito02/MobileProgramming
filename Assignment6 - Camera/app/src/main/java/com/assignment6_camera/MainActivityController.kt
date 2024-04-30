package com.assignment6_camera

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.format.DateFormat
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.Date

class MainActivityController(private val context: Context, private val callback: UpdateImageViewCallback) {
    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun saveImageFile(intent : Intent?) {
        try {
            if (intent == null) {
                throw Exception("Error passing image file!")
            }

            val imageBitmap : Bitmap = intent.getParcelableExtra("data", Bitmap::class.java)
                ?: throw Exception("Error passing image file!")

//            val byteOutputStream = ByteArrayOutputStream()
//            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteOutputStream)
//            val byteArray : ByteArray = byteOutputStream.toByteArray()
//
//            // save to external storage
//            val dir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString())
//            val date = Date()
//            val dateNow = DateFormat.format("MM-dd-yyyy hh-mm-ss", date.time)
//            val imageUri = File(dir, "capture_${dateNow}")

            val dirUri : Uri
            val contentResolver = context.contentResolver

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                dirUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                dirUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val contentValues = ContentValues()
            val date = Date()
            val dateNow = DateFormat.format("MM-dd-yyyy hh-mm-ss", date.time)
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "${dateNow}.png")
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/")

            val imageUri : Uri? = contentResolver.insert(dirUri, contentValues)

            try {
                val outputStream = contentResolver.openOutputStream(imageUri!!)
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!)

                Toast.makeText(context, "Image saved to the gallery", Toast.LENGTH_SHORT).show()
            } catch (ex : Exception) {
                Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
                throw ex
            }

            Toast.makeText(context, "Image has been saved to the Gallery", Toast.LENGTH_SHORT).show()

            // update the image view
            callback.updateImageView(imageBitmap)

        } catch (ex : Exception) {
            throw ex
        }
    }
}