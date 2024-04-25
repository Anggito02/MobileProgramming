package com.assignment5_jsonparser

import java.io.Serializable

data class Contact (
    val id : String,
    val name : String,
    val email : String,
    val address : String,
    val gender : String,
    val phoneMobile : String,
    val phoneHome : String,
    val phoneOffice : String
) : Serializable
