package com.example.assignment3_crudsqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity(), OnClickListener {
    lateinit var Btn_Login : Button
    lateinit var Btn_Register : Button
    lateinit var ETxt_Username : EditText
    lateinit var ETxt_Password : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Find resources
        Btn_Login = findViewById(R.id.btn_login)
        Btn_Register = findViewById(R.id.btn_register)
        ETxt_Username = findViewById(R.id.etxt_username)
        ETxt_Password = findViewById(R.id.etxt_password)

        // Set Listener
        Btn_Login.setOnClickListener(this)
        Btn_Register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_login -> {
                val username: String = ETxt_Username.text.toString()
                val password: String = ETxt_Password.text.toString()

                val user = _getUserByLogin(username, password)

                if (user != null) {
                    ETxt_Username.text.clear()
                    ETxt_Password.text.clear()

                    _openHomeActivity(user)
                } else {
                    Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_LONG).show()
                }
            }

            R.id.btn_register -> {
                _openRegisterActivity()
            }
        }
    }

    private fun _getUserByLogin(usernameInput: String, passwordInput: String): User? {
        val db = DatabaseHandler(this, null)
        val cursor = db.getUserByLogin(usernameInput, passwordInput)

        cursor?.use {
            if (cursor.moveToFirst()) {
                val idIndex = cursor.getColumnIndex("id")
                val usernameIndex = cursor.getColumnIndex("username")
                val passwordIndex = cursor.getColumnIndex("password")
                val emailIndex = cursor.getColumnIndex("email")
                val firstNameIndex = cursor.getColumnIndex("firstName")
                val lastNameIndex = cursor.getColumnIndex("lastName")

                val id = cursor.getInt(idIndex).toString()
                val username = cursor.getString(usernameIndex)
                val password = cursor.getString(passwordIndex)
                val email = cursor.getString(emailIndex)
                val firstName = cursor.getString(firstNameIndex)
                val lastName = cursor.getString(lastNameIndex)

                return User(
                    id = id,
                    username = username,
                    password = password,
                    email = email,
                    firstName = firstName,
                    lastName = lastName
                )
            }
        }
        return null
    }

    private fun _openHomeActivity(userData: User) {
        val activityIntent: Intent = Intent(this, HomeActivity::class.java)
        activityIntent.putExtra("user", userData)
        startActivity(activityIntent)
    }

    private fun _openRegisterActivity() {
        val activityIntent: Intent = Intent(this, RegisterActivity::class.java)
        startActivity(activityIntent)
    }
}