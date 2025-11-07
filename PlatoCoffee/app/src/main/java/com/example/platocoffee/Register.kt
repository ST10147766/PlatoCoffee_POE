package com.example.platocoffee

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class Register : AppCompatActivity() {

    private lateinit var btnRegister: Button
    private lateinit var tvLoginLink: TextView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize views
        btnRegister = findViewById(R.id.btnRegister)
        tvLoginLink = findViewById(R.id.tvLoginLink)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        // Navigate to Login screen
        tvLoginLink.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        // Handle registration
        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validate inputs
        if (TextUtils.isEmpty(name)) {
            etName.error = "Full name is required"
            etName.requestFocus()
            return
        }
        if (TextUtils.isEmpty(email)) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return
        }
        if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            etPassword.requestFocus()
            return
        }

        // Show loading state
        btnRegister.text = "Creating Account..."
        btnRegister.isEnabled = false

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = mAuth.currentUser
                    firebaseUser?.let { user ->
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()

                        user.updateProfile(profileUpdates)
                            .addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    // Send verification email
                                    user.sendEmailVerification()
                                        .addOnCompleteListener { verifyTask ->
                                            if (verifyTask.isSuccessful) {
                                                Toast.makeText(
                                                    this,
                                                    "Registration successful! Please check your email for verification.",
                                                    Toast.LENGTH_LONG
                                                ).show()

                                                // Navigate to Login screen
                                                val intent = Intent(this, Login::class.java)
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                Toast.makeText(
                                                    this,
                                                    "Registered successfully, but failed to send verification email.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                resetButton()
                                            }
                                        }
                                } else {
                                    Toast.makeText(
                                        this,
                                        "Registered successfully, but failed to update profile.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    resetButton()
                                }
                            }
                    }
                } else {
                    val errorMsg = task.exception?.message ?: "Registration failed"
                    Toast.makeText(this, "Registration failed: $errorMsg", Toast.LENGTH_LONG).show()
                    resetButton()
                }
            }
    }

    private fun resetButton() {
        btnRegister.text = "Register"
        btnRegister.isEnabled = true
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
