package com.assignment5_jsonparser

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class ContactsAdapter(private val context: Context, private val dataSource: ArrayList<Contact>): BaseAdapter() {
    private val layoutInflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Contact {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val contactRow = layoutInflater.inflate(R.layout.contact_row, parent, false)
        val LL_MainLayout : LinearLayout = contactRow.findViewById(R.id.LL_main_layout)
        val IV_ProfilePict : ImageView = contactRow.findViewById(R.id.IV_profile_pict)
        val TV_ContactName : TextView = contactRow.findViewById(R.id.main_contact_name)
        val TV_EmailName : TextView = contactRow.findViewById(R.id.main_contact_email)

        val contactData : Contact = getItem(position)

        // Set contact name & email info
        TV_ContactName.text = contactData.name
        TV_EmailName.text = contactData.email

        // Set profile pict
        if (contactData.gender == "female") {
            IV_ProfilePict.setImageResource(R.drawable.user_profile_female)
        } else {
            IV_ProfilePict.setImageResource(R.drawable.user_profile_male)
        }

        // Contact Detail Listener
        LL_MainLayout.setOnClickListener {
            val contact = getItem(position)
            val activityIntent = Intent(context, ContactDetailActivity::class.java)
            activityIntent.putExtra("contact", contact)
            context.startActivity(activityIntent)
        }

        return contactRow
    }
}