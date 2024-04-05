package com.example.assignment3_crudsqlite


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + USER_TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY, " +
                COL_USERNAME + " TEXT," +
                COL_PASSWORD + " TEXT," +
                COL_EMAIL + " TEXT," +
                COL_FIRSTNAME + " TEXT," +
                COL_LASTNAME + " TEXT)")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE)
        onCreate(db)
    }

    fun addUser(firstName: String, lastName: String, email: String, username: String, password: String): Boolean {
        try {
            val values = ContentValues()

            values.put(COL_USERNAME, username)
            values.put(COL_PASSWORD, password)
            values.put(COL_EMAIL, email)
            values.put(COL_FIRSTNAME, firstName)
            values.put(COL_LASTNAME, lastName)

            val db = this.writableDatabase
            db.insert(USER_TABLE, null, values)

            db.close()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun getUserByLogin(username: String, password: String): Cursor? {
        val db = this.readableDatabase
        val selection = "username = ? AND password = ?"
        val selectionArgs = arrayOf(username, password)

        return db.query(USER_TABLE, null, selection, selectionArgs, null, null, null)
    }

    fun editUser(userData: User): Boolean {
        try {
            val values = ContentValues()

            values.put(COL_FIRSTNAME, userData.firstName)
            values.put(COL_LASTNAME, userData.lastName)
            values.put(COL_EMAIL, userData.email)
            values.put(COL_USERNAME, userData.username)
            values.put(COL_PASSWORD, userData.password)

            val db = this.writableDatabase
            val selection = "id = ?"
            val selectionArgs = arrayOf(userData.id)

            db.update(USER_TABLE, values, selection, selectionArgs)
            db.close()

            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun deleteUser(userId: String): Boolean {
        try {
            val db = this.writableDatabase
            val selection = "id = ?"
            val selectionArgs = arrayOf(userId)

            db.delete(USER_TABLE, selection, selectionArgs)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    companion object {
        private val DATABASE_NAME = "db_app_user"
        private val DATABASE_VERSION = 2

        val USER_TABLE = "users"

        val COL_ID = "id"
        val COL_USERNAME = "username"
        val COL_PASSWORD = "password"
        val COL_EMAIL = "email"
        val COL_FIRSTNAME = "firstName"
        val COL_LASTNAME = "lastName"
    }

}