package com.example.assignment3_crudsqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity(), OnClickListener {
    lateinit var ETxt_FirstName : EditText
    lateinit var ETxt_LastName : EditText
    lateinit var ETxt_Email : EditText
    lateinit var ETxt_Username : EditText
    lateinit var ETxt_Password : EditText
    lateinit var Btn_Register : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Find resources
        ETxt_FirstName = findViewById(R.id.etxt_first_name)
        ETxt_LastName = findViewById(R.id.etxt_last_name)
        ETxt_Email = findViewById(R.id.etxt_email)
        ETxt_Username = findViewById(R.id.etxt_username)
        ETxt_Password = findViewById(R.id.etxt_password)
        Btn_Register = findViewById(R.id.btn_register)

        // Set Listener
        Btn_Register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_register -> {
                val firstName: String = ETxt_FirstName.text.toString()
                val lastName: String = ETxt_LastName.text.toString()
                val email: String = ETxt_Email.text.toString()
                val username: String = ETxt_Username.text.toString()
                val password: String = ETxt_Password.text.toString()

                val result = _addNewUser(firstName, lastName, email, username, password)

                if (!result) {
                    Toast.makeText(this, "Create account failed!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Account created successfully!", Toast.LENGTH_LONG).show()
                    _openLoginActivity()
                }
            }
        }
    }

    private fun _addNewUser(firstNameInput: String, lastNameInput: String, emailInput: String, usernameInput: String, passwordInput: String): Boolean {
        val db = DatabaseHandler(this, null)
        return db.addUser(firstNameInput, lastNameInput, emailInput, usernameInput, passwordInput)
    }

    private fun _openLoginActivity() {
        val activityIntent: Intent = Intent(this, LoginActivity::class.java)
        startActivity(activityIntent)
    }
}