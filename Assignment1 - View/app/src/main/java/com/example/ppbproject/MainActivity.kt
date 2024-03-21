package com.example.ppbproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

class MainActivity : AppCompatActivity() , OnClickListener {
    private lateinit var Btn_NavButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Btn_NavButton = findViewById<Button>(R.id.nav_button)
        Btn_NavButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.nav_button -> {
                _openExpActivity()
            }
        }
    }

    private fun _openExpActivity() {
        val activityIntent : Intent = Intent(this, WorkExpActivity::class.java)
        startActivity(activityIntent)
    }
}