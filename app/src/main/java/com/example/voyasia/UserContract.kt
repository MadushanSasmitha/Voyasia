// Create a new Kotlin file, e.g., UserContract.kt
package com.example.voyasia // Or your preferred package

import android.provider.BaseColumns

object UserContract {
    // Inner class that defines the table contents
    object UserEntry : BaseColumns { // BaseColumns provides _ID
        const val TABLE_NAME = "users"
        const val COLUMN_NAME_FULL_NAME = "full_name" // From signup
        const val COLUMN_NAME_EMAIL = "email"
        const val COLUMN_NAME_PASSWORD = "password" // STORE HASHED PASSWORDS IN PRODUCTION
    }
}
