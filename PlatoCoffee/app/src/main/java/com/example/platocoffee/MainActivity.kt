package com.example.platocoffee

import android.content.Intent
import android.os.Bundle
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private var coffeeGrid: GridView? = null
    private var profileIcon: ImageView? = null
    private var currentUserEmail: String? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Get the logged-in user email
        loggedInUser
        initializeViews()
        setupCoffeeItems()
        setupClickListeners()
    }

    private val loggedInUser: Unit
        private get() {
            // Method 1: Get from Firebase Auth (most reliable)
            val currentUser = mAuth!!.currentUser
            currentUserEmail = if (currentUser != null) {
                currentUser.email
            } else {
                // Method 2: Get from Intent (from Login activity)
                if (intent != null && intent.hasExtra("user_email")) {
                    intent.getStringExtra("user_email")
                } else {
                    // Method 3: Get from SharedPreferences (fallback)
                    val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    prefs.getString("user_email", "user@example.com")
                }
            }

            // Save to SharedPreferences for consistency
            if (currentUserEmail != null && currentUserEmail != "user@example.com") {
                saveUserToPrefs(currentUserEmail!!)
            }
        }

    private fun saveUserToPrefs(email: String) {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("user_email", email)
        editor.apply()
    }

    private fun initializeViews() {
        coffeeGrid = findViewById(R.id.coffeeGrid)
        profileIcon = findViewById(R.id.profileIcon)
    }

    private fun setupClickListeners() {
        // Profile icon click listener
        profileIcon!!.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            // Pass the current user email to ProfileActivity
            intent.putExtra("user_email", currentUserEmail)
            startActivity(intent)
        }
    }

    private fun setupCoffeeItems() {
        val coffeeItems = dummyCoffeeData
        val adapter = CoffeeAdapter(this, coffeeItems)
        coffeeGrid!!.adapter = adapter
    }

    private val dummyCoffeeData: List<CoffeeItem>
        private get() {
            val coffeeItems: MutableList<CoffeeItem> = ArrayList()

            // Cappuccino items
            coffeeItems.add(
                CoffeeItem(
                    4.8f,
                    "Cappuccino Classic",
                    45.13,
                    "Cappuccino",
                    R.drawable.ic_cup
                )
            )
            coffeeItems.add(
                CoffeeItem(
                    3.0f,
                    "Cappuccino Chiaro",
                    64.53,
                    "Cappuccino",
                    R.drawable.ic_cup1
                )
            )
            coffeeItems.add(
                CoffeeItem(
                    4.8f,
                    "Cappuccino Sciuro",
                    75.50,
                    "Cappuccino",
                    R.drawable.ic_cup2
                )
            )
            coffeeItems.add(
                CoffeeItem(
                    3.0f,
                    "Cappuccino with Chocolate",
                    75.50,
                    "Cappuccino",
                    R.drawable.ic_cup
                )
            )

            // Macchiato items
            coffeeItems.add(
                CoffeeItem(
                    4.5f,
                    "Caramel Macchiato",
                    55.25,
                    "Macchiato",
                    R.drawable.ic_mochi
                )
            )
            coffeeItems.add(
                CoffeeItem(
                    4.2f,
                    "Hazelnut Macchiato",
                    52.75,
                    "Macchiato",
                    R.drawable.ic_mochi1
                )
            )

            // Latte items
            coffeeItems.add(CoffeeItem(4.7f, "Vanilla Latte", 48.90, "Latte", R.drawable.ic_lattie))
            coffeeItems.add(
                CoffeeItem(
                    4.9f,
                    "Caramel Latte",
                    52.40,
                    "Latte",
                    R.drawable.ic_lattie2
                )
            )

            // Decaf items
            coffeeItems.add(
                CoffeeItem(
                    4.3f,
                    "Decaf Americano",
                    42.15,
                    "Decaf",
                    R.drawable.ic_decaf
                )
            )
            coffeeItems.add(
                CoffeeItem(
                    4.1f,
                    "Decaf Espresso",
                    38.75,
                    "Decaf",
                    R.drawable.ic_decaf1
                )
            )
            return coffeeItems
        }

    override fun onStart() {
        super.onStart()
        // Check if user is still logged in
        val currentUser = mAuth!!.currentUser
        if (currentUser == null) {
            // User is not logged in, redirect to Login
            val intent = Intent(this@MainActivity, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}