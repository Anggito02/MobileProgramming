package com.assignment5_jsonparser

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi

class ContactDetailActivity : AppCompatActivity(), OnClickListener {
    lateinit var IB_BackToMain : ImageButton
    lateinit var IV_ProfilePicture : ImageView
    lateinit var TV_Name : TextView
    lateinit var TV_Email : TextView
    lateinit var TV_Address : TextView
    lateinit var TV_Gender : TextView
    lateinit var TV_Phone : TextView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        IB_BackToMain = findViewById(R.id.detail_IB_back_to_main)
        IV_ProfilePicture = findViewById(R.id.detail_IV_profile_pict)
        TV_Name = findViewById(R.id.detail_TV_name)
        TV_Email = findViewById(R.id.detail_TV_email)
        TV_Address = findViewById(R.id.detail_TV_address)
        TV_Gender = findViewById(R.id.detail_TV_gender)
        TV_Phone = findViewById(R.id.detail_TV_phone)

        // Set Listener
        IB_BackToMain.setOnClickListener(this)

        _setContactDetail()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun _setContactDetail() {
        try {
            // Get contact detail information
            val contact: Contact? = intent.getSerializableExtra("contact", Contact::class.java)
            if (contact == null) {
                throw Exception("Failed passing contact detail")
            }

            // Set profile picture
            if (contact.gender == "female") {
                IV_ProfilePicture.setImageResource(R.drawable.user_profile_female)
            } else {
                IV_ProfilePicture.setImageResource(R.drawable.user_profile_male)
            }

            // Set other contact detail
            TV_Name.text = contact.name
            TV_Email.text = contact.email
            TV_Address.text = contact.address
            TV_Gender.text = contact.gender

            val phones = "${contact.phoneMobile}\n${contact.phoneHome}\n${contact.phoneOffice}"
            TV_Phone.text = phones

            return
        } catch (ex : Exception) {
            ex.printStackTrace()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.detail_IB_back_to_main -> {
                val activityIntent = Intent(this, MainActivity::class.java)
                startActivity(activityIntent)
                this.finish()
            }
        }
    }
}