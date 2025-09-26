// In your SignupActivity.kt
package com.example.voyasia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignupActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDbHelper

    private lateinit var fullNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var confirmPasswordEditText: TextInputEditText

    private lateinit var fullNameInputLayout: TextInputLayout
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout

    private lateinit var termsCheckbox: CheckBox
    private lateinit var signupButton: Button
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup) // Make sure this matches your XML file name

        dbHelper = UserDbHelper(this)

        // Initialize Views
        fullNameEditText = findViewById(R.id.edit_text_full_name)
        emailEditText = findViewById(R.id.edit_text_email_signup)
        passwordEditText = findViewById(R.id.edit_text_password_signup)
        confirmPasswordEditText = findViewById(R.id.edit_text_confirm_password)

        fullNameInputLayout = findViewById(R.id.full_name_text_input_layout)
        emailInputLayout = findViewById(R.id.email_text_input_layout_signup)
        passwordInputLayout = findViewById(R.id.password_text_input_layout_signup)
        confirmPasswordInputLayout = findViewById(R.id.confirm_password_text_input_layout)

        termsCheckbox = findViewById(R.id.terms_and_conditions_checkbox)
        signupButton = findViewById(R.id.signup_button)
        loginLink = findViewById(R.id.login_link)

        signupButton.setOnClickListener {
            performSignup()
        }

        loginLink.setOnClickListener {
            // Intent to navigate back to your LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Optional: finish SignupActivity
        }

        // Optional: Make "Terms and Conditions" text clickable (more advanced)
        // You might need to use SpannableString here if only part of the checkbox text is a link
        termsCheckbox.setOnCheckedChangeListener { _, isChecked ->
            // You can enable/disable signup button based on this, or just validate on click
        }
    }

    private fun performSignup() {
        val fullName = fullNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim() // STORE HASHED IN PRODUCTION
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        // --- Validation ---
        var isValid = true

        // Full Name Validation
        if (fullName.isEmpty()) {
            fullNameInputLayout.error = "Full name cannot be empty"
            isValid = false
        } else {
            fullNameInputLayout.error = null
        }

        // Email Validation
        if (email.isEmpty()) {
            emailInputLayout.error = "Email cannot be empty"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.error = "Enter a valid email address"
            isValid = false
        } else {
            emailInputLayout.error = null
        }

        // Password Validation
        if (password.isEmpty()) {
            passwordInputLayout.error = "Password cannot be empty"
            isValid = false
        } else if (password.length < 6) { // Example: Minimum password length
            passwordInputLayout.error = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordInputLayout.error = null
        }

        // Confirm Password Validation
        if (confirmPassword.isEmpty()) {
            confirmPasswordInputLayout.error = "Please confirm your password"
            isValid = false
        } else if (password != confirmPassword) {
            confirmPasswordInputLayout.error = "Passwords do not match"
            isValid = false
        } else {
            confirmPasswordInputLayout.error = null
        }

        // Terms and Conditions Check
        if (!termsCheckbox.isChecked) {
            Toast.makeText(this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show()
            // Optionally, you could set an error directly on the checkbox or a nearby TextView
            isValid = false
        }

        if (!isValid) {
            return // Stop if validation fails
        }

        // --- Add User to Database ---
        // SECURITY WARNING: Hash the password before storing it in a real application!
        val isUserAdded = dbHelper.addUser(fullName, email, password)

        if (isUserAdded) {
            Toast.makeText(this, "Signup Successful! Please login.", Toast.LENGTH_LONG).show()
            // Navigate to LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear back stack
            startActivity(intent)
            finish() // Finish SignupActivity
        } else {
            // This might happen if the email is already taken (due to UNIQUE constraint)
            // or some other database error.
            Toast.makeText(this, "Signup Failed. Email might already exist.", Toast.LENGTH_LONG).show()
            emailInputLayout.error = "Email might already be registered" // Example specific error
        }
    }

    override fun onDestroy() {
        dbHelper.close() // Close the database connection
        super.onDestroy()
    }
}

