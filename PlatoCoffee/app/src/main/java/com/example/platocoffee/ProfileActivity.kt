package com.example.platocoffee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private var currentPasswordEditText: EditText? = null
    private var newPasswordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var updatePasswordButton: Button? = null
    private var logoutButton: Button? = null
    private var backButton: ImageView? = null
    private var userEmailText: TextView? = null
    private var userEmail: String? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Get user email from Intent
        userEmailFromIntent
        initializeViews()
        setupClickListeners()
        displayUserEmail()
    }

    private val userEmailFromIntent: Unit
        private get() {
            val intent = intent
            userEmail = if (intent != null && intent.hasExtra("user_email")) {
                intent.getStringExtra("user_email")
            } else {
                // Fallback to SharedPreferences if no intent data
                val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                prefs.getString("user_email", "user@example.com")
            }
        }

    private fun initializeViews() {
        currentPasswordEditText = findViewById(R.id.currentPassword)
        newPasswordEditText = findViewById(R.id.newPassword)
        confirmPasswordEditText = findViewById(R.id.confirmPassword)
        updatePasswordButton = findViewById(R.id.updatePasswordButton)
        logoutButton = findViewById(R.id.logoutButton)
        backButton = findViewById(R.id.backButton)
        userEmailText = findViewById(R.id.userEmailText)
    }

    private fun setupClickListeners() {
        // Back button
        backButton!!.setOnClickListener { finish() }

        // Update password button
        updatePasswordButton!!.setOnClickListener { updatePassword() }

        // Logout button
        logoutButton!!.setOnClickListener { logoutUser() }
    }

    private fun displayUserEmail() {
        userEmailText!!.text = userEmail
    }

    private fun updatePassword() {
        val currentPassword = currentPasswordEditText!!.text.toString().trim { it <= ' ' }
        val newPassword = newPasswordEditText!!.text.toString().trim { it <= ' ' }
        val confirmPassword = confirmPasswordEditText!!.text.toString().trim { it <= ' ' }

        // Validation
        if (currentPassword.isEmpty()) {
            showToast("Please enter your current password")
            return
        }
        if (newPassword.isEmpty()) {
            showToast("Please enter a new password")
            return
        }
        if (confirmPassword.isEmpty()) {
            showToast("Please confirm your new password")
            return
        }
        if (newPassword != confirmPassword) {
            showToast("New passwords don't match")
            return
        }
        if (newPassword.length < 8) {
            showToast("Password must be at least 8 characters long")
            return
        }

        // Here you would typically verify current password and update
        // For now, just show success message
        showToast("Password updated successfully for $userEmail")

        // Clear fields
        currentPasswordEditText!!.setText("")
        newPasswordEditText!!.setText("")
        confirmPasswordEditText!!.setText("")
    }

    private fun logoutUser() {
        // Sign out from Firebase
        mAuth!!.signOut()

        // Clear SharedPreferences
        clearUserPreferences()

        // Show logout message
        showToast("Logged out successfully")

        // Redirect to Login activity
        val intent = Intent(this@ProfileActivity, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun clearUserPreferences() {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove("user_email")
        editor.apply()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}