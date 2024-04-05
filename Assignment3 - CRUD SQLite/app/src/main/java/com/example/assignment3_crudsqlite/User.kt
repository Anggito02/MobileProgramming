package com.example.assignment3_crudsqlite

import java.io.Serializable

data class User(
    var id: String,
    var username: String,
    var password: String,
    var email: String,
    var firstName: String,
    var lastName:String
): Serializable