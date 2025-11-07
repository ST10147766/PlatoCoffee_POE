package com.example.platocoffee

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Get reference to the Get Started button
        val getStartedBtn = findViewById<Button>(R.id.getStartedBtn)

        // Set a click listener
        getStartedBtn.setOnClickListener {
            // Navigate to MainActivity
            val intent = Intent(this@Splash, Register::class.java)
            startActivity(intent)
            finish() // Optional: remove splash from back stack
        }
    }
}