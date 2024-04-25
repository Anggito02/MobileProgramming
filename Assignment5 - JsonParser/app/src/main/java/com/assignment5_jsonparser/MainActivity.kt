package com.assignment5_jsonparser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView

class MainActivity : AppCompatActivity(), FetchDataCallback {
    private val controller = MainActivityController(this, this)

    lateinit var LV_Contacts : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LV_Contacts = findViewById(R.id.LV_contacts)

        controller.fetchData(resources.getString(R.string.CONTACTS_URL))
    }

    override fun onDataFetched(contactList: ArrayList<Contact>) {
        val contactsAdapter = ContactsAdapter(this, contactList)
        LV_Contacts.adapter = contactsAdapter
    }
}