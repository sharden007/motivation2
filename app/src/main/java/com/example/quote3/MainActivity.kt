package com.example.quote3

import android.view.View
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.inspirationalquotes.DatabaseHelper
import com.example.quote3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Use ViewBinding for layout inflation
    private lateinit var binding: ActivityMainBinding

    // Use DatabaseHelper directly to fetch quotes
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)

        // Inflate layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load a random quote when the activity starts
        binding.loadingSpinner.visibility = View.VISIBLE // Show loading spinner initially
        loadRandomQuote()
    }

    /**
     * Fetches a random quote from the database and updates the UI.
     */
    private fun loadRandomQuote() {
        val randomQuote = dbHelper.getRandomQuote() ?: "No inspirational quotes available."

        // Update UI with the fetched quote
        binding.loadingSpinner.visibility = View.GONE // Hide loading spinner
        binding.quoteText.visibility = View.VISIBLE   // Show quote text
        binding.quoteText.text = randomQuote          // Display the fetched quote
    }
}
