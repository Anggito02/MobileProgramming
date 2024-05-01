package com.assignment6_camera

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap

class MainActivity : AppCompatActivity(), OnClickListener, MainActivityCallback {
    private val _controller = MainActivityController(this, this)
    lateinit var _cameraActivityResultLauncher : ActivityResultLauncher<Intent>

    lateinit var IV_CameraView : ImageView
    lateinit var Btn_TakePicture : Button
    lateinit var Btn_Upload : Button

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _cameraActivityResultLauncher = _getCameraActivityResultLauncher()

        IV_CameraView = findViewById(R.id.IV_camera_view)
        Btn_TakePicture = findViewById(R.id.Btn_take_pict)
        Btn_Upload = findViewById(R.id.Btn_upload)

        Btn_TakePicture.setOnClickListener(this)
        Btn_Upload.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Btn_take_pict -> {
                try {
                    // Take and save image
                    val cameraActivityIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    _cameraActivityResultLauncher.launch(cameraActivityIntent)
                } catch (ex : Exception) {
                    ex.printStackTrace()
                }
            }

            R.id.Btn_upload -> {
                try {
                    val imageByteArray : ByteArray = _controller.bitmapToByteArray(IV_CameraView.drawable.toBitmap())

                    _controller.uploadImage(resources.getString(R.string.BACKEND_URL), imageByteArray)
                } catch (ex : Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun updateImageView(imageBitmap : Bitmap) {
        IV_CameraView.setImageBitmap(imageBitmap)
    }

    override fun showUploadButton() {
        Btn_Upload.visibility = View.VISIBLE
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun _getCameraActivityResultLauncher() : ActivityResultLauncher<Intent> {
        try {
            val activityResultLauncher = registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data : Intent? = result.data
                    _controller.saveImageFile(data)
                }
            }

            return activityResultLauncher
        } catch (ex : Exception) {
            throw ex
        }
    }
}