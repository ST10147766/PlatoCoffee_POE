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

class Login : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var tvRegisterLink: TextView
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize views
        btnLogin = findViewById(R.id.btnLogin)
        tvRegisterLink = findViewById(R.id.tvRegisterLink)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        // Navigate to Register screen
        tvRegisterLink.setOnClickListener {
            val intent = Intent(this@Login, Register::class.java)
            startActivity(intent)
        }

        // Handle login
        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()

        // Validate inputs
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

        // Show loading state
        btnLogin.text = "Logging in..."
        btnLogin.isEnabled = false

        // Sign in with Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth.currentUser
                    if (user != null) {
                        if (user.isEmailVerified) {
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                            saveUserToPrefs(user.email)

                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("user_email", user.email)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                "Please verify your email address before logging in.",
                                Toast.LENGTH_LONG
                            ).show()
                            mAuth.signOut()
                            resetLoginButton()
                        }
                    }
                } else {
                    var errorMessage = "Login failed"
                    task.exception?.let { e ->
                        errorMessage = e.message ?: errorMessage
                        when {
                            errorMessage.contains("invalid credential", true) ||
                                    errorMessage.contains("password is invalid", true) ->
                                errorMessage = "Invalid email or password"

                            errorMessage.contains("user not found", true) ->
                                errorMessage = "No account found with this email"

                            errorMessage.contains("network error", true) ->
                                errorMessage = "Network error. Please check your connection"
                        }
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    resetLoginButton()
                }
            }
    }

    private fun saveUserToPrefs(email: String?) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("user_email", email)
        editor.apply()
    }

    private fun resetLoginButton() {
        btnLogin.text = "Login"
        btnLogin.isEnabled = true
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null && currentUser.isEmailVerified) {
            saveUserToPrefs(currentUser.email)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user_email", currentUser.email)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        resetLoginButton()
    }
}
