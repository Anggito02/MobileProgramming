package com.example.assignment3_crudsqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.EditText
import android.widget.ToggleButton
import android.widget.TextView
import android.widget.Toast
import java.io.Serializable

class HomeActivity : AppCompatActivity(),OnClickListener, OnCheckedChangeListener {
    var userId: String = ""

    lateinit var TV_Title: TextView
    lateinit var ETxt_FirstName: EditText
    lateinit var ETxt_LastName: EditText
    lateinit var ETxt_Email: EditText
    lateinit var ETxt_Username: EditText
    lateinit var ETxt_Password: EditText

    lateinit var ToggleBtnEdit_FirstName: ToggleButton
    lateinit var ToggleBtnEdit_LastName: ToggleButton
    lateinit var ToggleBtnEdit_Email: ToggleButton
    lateinit var ToggleBtnEdit_Username: ToggleButton
    lateinit var ToggleBtnEdit_Password: ToggleButton

    lateinit var Btn_Edit: Button
    lateinit var Btn_Delete: Button
    lateinit var Btn_Logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Get intent data
        val user: User = intent.getSerializableExtra("user") as User

        // Find resources
        TV_Title = findViewById(R.id.txt_title)
        ETxt_FirstName = findViewById(R.id.home_first_name)
        ETxt_LastName = findViewById(R.id.home_last_name)
        ETxt_Email = findViewById(R.id.home_email)
        ETxt_Username = findViewById(R.id.home_username)
        ETxt_Password = findViewById(R.id.home_password)

        ToggleBtnEdit_FirstName = findViewById(R.id.toggle_btn_first_name)
        ToggleBtnEdit_LastName = findViewById(R.id.toggle_btn_last_name)
        ToggleBtnEdit_Email = findViewById(R.id.toggle_btn_email)
        ToggleBtnEdit_Username = findViewById(R.id.toggle_btn_username)
        ToggleBtnEdit_Password = findViewById(R.id.toggle_btn_password)

        Btn_Edit = findViewById(R.id.btn_edit)
        Btn_Delete = findViewById(R.id.btn_delete)
        Btn_Logout = findViewById(R.id.btn_logout)

        // Set Listeners
        ToggleBtnEdit_FirstName.setOnCheckedChangeListener(this)
        ToggleBtnEdit_LastName.setOnCheckedChangeListener(this)
        ToggleBtnEdit_Email.setOnCheckedChangeListener(this)
        ToggleBtnEdit_Username.setOnCheckedChangeListener(this)
        ToggleBtnEdit_Password.setOnCheckedChangeListener(this)

        Btn_Edit.setOnClickListener(this)
        Btn_Delete.setOnClickListener(this)
        Btn_Logout.setOnClickListener(this)

        // Set Initial Data
        _setInitialData(user)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit -> {
                val newUserData = _fetchUserData()
                if (!_editUser(newUserData)) {
                    Toast.makeText(this, "Failed to update your data", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this, "Your data had been updated!", Toast.LENGTH_LONG).show()
                }
            }

            R.id.btn_delete -> {
                if (!_deleteUser()) {
                    Toast.makeText(this, "Failed to delete your data!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Your data had been deleted!", Toast.LENGTH_LONG).show()
                    val activityIntent: Intent = Intent(this, LoginActivity::class.java)
                    startActivity(activityIntent)
                }
            }

            R.id.btn_logout -> {
                val activityIntent: Intent = Intent(this, LoginActivity::class.java)
                startActivity(activityIntent)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView?.id) {
            R.id.toggle_btn_first_name -> {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.cancel)
                    ETxt_FirstName.isEnabled = true
                }
                else {
                    buttonView.setBackgroundResource(R.drawable.edit_icon)
                    ETxt_FirstName.isEnabled = false
                }
            }

            R.id.toggle_btn_last_name -> {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.cancel)
                    ETxt_LastName.isEnabled = true
                }
                else {
                    buttonView.setBackgroundResource(R.drawable.edit_icon)
                    ETxt_LastName.isEnabled = false
                }
            }

            R.id.toggle_btn_email -> {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.cancel)
                    ETxt_Email.isEnabled = true
                }
                else {
                    buttonView.setBackgroundResource(R.drawable.edit_icon)
                    ETxt_Email.isEnabled = false
                }
            }

            R.id.toggle_btn_username -> {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.cancel)
                    ETxt_Username.isEnabled = true
                }
                else {
                    buttonView.setBackgroundResource(R.drawable.edit_icon)
                    ETxt_Username.isEnabled = false
                }
            }

            R.id.toggle_btn_password -> {
                if (isChecked) {
                    buttonView.setBackgroundResource(R.drawable.cancel)
                    ETxt_Password.isEnabled = true
                }
                else {
                    buttonView.setBackgroundResource(R.drawable.edit_icon)
                    ETxt_Password.isEnabled = false
                }
            }
        }
    }

    private fun _setInitialData(userData: User) {
        userId = userData.id

        val title: String = "Hello, " + userData.username + "!"

        TV_Title.text = title
        ETxt_FirstName.setText(userData.firstName)
        ETxt_LastName.setText(userData.lastName)
        ETxt_Email.setText(userData.email)
        ETxt_Username.setText(userData.username)
        ETxt_Password.setText(userData.password)
    }

    private fun _fetchUserData(): User {
        return User(
            id = userId,
            firstName = ETxt_FirstName.text.toString(),
            lastName = ETxt_LastName.text.toString(),
            email = ETxt_Email.text.toString(),
            username = ETxt_Username.text.toString(),
            password = ETxt_Password.text.toString()
        )
    }

    private fun _editUser(userData: User): Boolean {
        val db = DatabaseHandler(this, null)
        return db.editUser(userData)
    }
    
    private fun _deleteUser(): Boolean {
        val db = DatabaseHandler(this, null)
        return db.deleteUser(userId)
    }
}
