// Create a new Kotlin file, e.g., UserDbHelper.kt
package com.example.voyasia

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.voyasia.UserContract.UserEntry

class UserDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "VoyasiaUser.db"

        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${UserEntry.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${UserEntry.COLUMN_NAME_FULL_NAME} TEXT," +
                    "${UserEntry.COLUMN_NAME_EMAIL} TEXT UNIQUE," + // Email should be unique
                    "${UserEntry.COLUMN_NAME_PASSWORD} TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${UserEntry.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    // --- Helper functions for user operations (you'll use these) ---

    // Add a new user (used by signup)
    fun addUser(fullName: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(UserEntry.COLUMN_NAME_FULL_NAME, fullName)
            put(UserEntry.COLUMN_NAME_EMAIL, email)
            put(UserEntry.COLUMN_NAME_PASSWORD, password) // REMEMBER TO HASH THIS
        }
        // Inserting Row. Returns -1 if error, otherwise row ID
        val newRowId = db.insert(UserEntry.TABLE_NAME, null, values)
        db.close()
        return newRowId != -1L // Return true if insert was successful
    }

    // Check if a user exists with the given email and password (used by login)
    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val columns = arrayOf(BaseColumns._ID) // We just need to check if a row exists
        val selection = "${UserEntry.COLUMN_NAME_EMAIL} = ? AND ${UserEntry.COLUMN_NAME_PASSWORD} = ?"
        val selectionArgs = arrayOf(email, password) // FOR HASHING: compare hashed input password with stored hash

        val cursor = db.query(
            UserEntry.TABLE_NAME,   // The table to query
            columns,             // The array of columns to return (pass null to get all)
            selection,              // The columns for the WHERE clause
            selectionArgs,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            null               // The sort order
        )
        val userExists = cursor.count > 0
        cursor.close()
        db.close()
        return userExists
    }

    // Optional: Get user details by email
    fun getUserByEmail(email: String): Map<String, String>? {
        val db = this.readableDatabase
        var user: Map<String, String>? = null
        val cursor = db.query(
            UserEntry.TABLE_NAME,
            arrayOf(BaseColumns._ID, UserEntry.COLUMN_NAME_FULL_NAME, UserEntry.COLUMN_NAME_EMAIL),
            "${UserEntry.COLUMN_NAME_EMAIL} = ?",
            arrayOf(email),
            null, null, null
        )
        with(cursor) {
            if (moveToFirst()) {
                val fullName = getString(getColumnIndexOrThrow(UserEntry.COLUMN_NAME_FULL_NAME))
                // You might not want to return the password, even if it's hashed
                user = mapOf("fullName" to fullName, "email" to email)
            }
        }
        cursor.close()
        db.close()
        return user
    }
}
