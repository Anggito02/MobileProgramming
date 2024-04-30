package com.assignment6_camera

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity(), OnClickListener, UpdateImageViewCallback {
    private val WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 223
    private val WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE

    private val _controller = MainActivityController(this, this)
    lateinit var _cameraActivityResultLauncher : ActivityResultLauncher<Intent>

    lateinit var IV_CameraView : ImageView
    lateinit var Btn_TakePicture : Button

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _cameraActivityResultLauncher = _getCameraActivityResultLauncher()
        _askWritePermission()

        IV_CameraView = findViewById(R.id.IV_camera_view)
        Btn_TakePicture = findViewById(R.id.Btn_take_pict)

        Btn_TakePicture.setOnClickListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.Btn_take_pict -> {
                try {
                    val cameraActivityIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    _cameraActivityResultLauncher.launch(cameraActivityIntent)
                } catch (ex : Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun updateImageView(imageBitmap : Bitmap) {
        IV_CameraView.setImageBitmap(imageBitmap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "WRITE_EXTERNAL_STORAGE Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun _askWritePermission() {
        val cameraPermission = checkSelfPermission(WRITE_EXTERNAL_STORAGE_PERMISSION)

        if (cameraPermission ==  PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "WRITE_EXTERNAL_STORAGE Access Granted", Toast.LENGTH_SHORT).show()
        } else {
            val alertDialogBuilder = AlertDialog.Builder(this)

            alertDialogBuilder
                .setMessage("This app requires WRITE_EXTERNAL_STORAGE permission")
                .setTitle("Permission Required")
                .setCancelable(false)
                .setPositiveButton("Allow") {
                        dialog, _ ->
                    val permissionsArray = arrayOf(WRITE_EXTERNAL_STORAGE_PERMISSION)
                    ActivityCompat.requestPermissions(this, permissionsArray, WRITE_EXTERNAL_STORAGE_PERMISSION_CODE)
                    dialog.dismiss()
                }
                .setNegativeButton("Don't Allow") {
                        _, _ -> finish()
                }

            alertDialogBuilder.show()
        }
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