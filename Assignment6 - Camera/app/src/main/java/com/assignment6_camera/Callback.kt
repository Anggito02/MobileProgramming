package com.assignment6_camera

import android.graphics.Bitmap

interface MainActivityCallback {
    fun updateImageView(imageBitmap : Bitmap)
    fun showUploadButton()
}