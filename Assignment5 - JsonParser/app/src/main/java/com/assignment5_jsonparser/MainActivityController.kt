package com.assignment5_jsonparser

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivityController(private val context : Context, private val callback : FetchDataCallback) {
    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val uiHandler = Handler(Looper.getMainLooper())

    fun fetchData(url: String) {
        Toast.makeText(context, "Fetching contacts ...", Toast.LENGTH_SHORT).show()

        executor.execute {
            val httpHandler = HttpHandler()
            val response : String = httpHandler.makeServiceCall(url)
            val contactList : ArrayList<Contact> = _responseToContactData(response)

            uiHandler.post {
                callback.onDataFetched(contactList)
            }
        }
    }

    private fun _responseToContactData(str: String): ArrayList<Contact> {
        try {
            val jsonObject = JSONObject(str)
            val contacts: JSONArray = jsonObject.getJSONArray("contacts")
            val contactList : ArrayList<Contact> = arrayListOf()

            for (i in 0..<contacts.length()) {
                val contact = contacts.getJSONObject(i)
                val phones = contact.getJSONObject("phone")

                val newContact = Contact(
                    contact.getString("id"),
                    contact.getString("name"),
                    contact.getString("email"),
                    contact.getString("address"),
                    contact.getString("gender"),
                    phones.getString("mobile"),
                    phones.getString("home"),
                    phones.getString("office")
                )

                contactList.add(newContact)
            }

            return contactList
        } catch (ex: Exception) {
            throw ex
        }
    }
}