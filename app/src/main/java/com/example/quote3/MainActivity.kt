package com.example.quote3

import androidx.appcompat.app.AppCompatActivity
import com.example.quote3.databinding.ActivityMainBinding
import com.example.inspirationalquotes.DatabaseHelper
import androidx.core.view.GestureDetectorCompat
import androidx.cardview.widget.CardView
import android.os.Bundle
import android.view.View
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize DatabaseHelper and ViewBinding
        dbHelper = DatabaseHelper(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set a random gradient background
        setRandomBackground()

        // Load a random quote initially
        loadRandomQuote()

        // Set up swipe gesture listener on the quote card
        binding.quoteCardFrame.setOnTouchListener(
            SwipeToLoadQuoteTouchListener(
                onSwipeLeft = { loadRandomQuote() }
            )
        )
    }

    /**
     * Sets a random gradient background from a predefined list of gradients.
     */
    private fun setRandomBackground() {
        // Array of gradient resources
        val gradients = arrayOf(
            R.drawable.background_gradient_1,
            R.drawable.background_gradient_2,
            R.drawable.background_gradient_3,
            R.drawable.background_gradient_4
        )

        // Generate a random index and set the background
        val randomIndex = Random.nextInt(gradients.size)
        binding.rootLayout.setBackgroundResource(gradients[randomIndex])
    }

    /**
     * Fetches a random quote from the database and updates the UI.
     */
    private fun loadRandomQuote() {
        // Show loading spinner while fetching the quote
        binding.loadingSpinner.visibility = View.VISIBLE
        binding.quoteText.visibility = View.GONE

        val randomQuote = dbHelper.getRandomQuote() ?: "No inspirational quotes available."

        // Update UI with the fetched quote
        binding.loadingSpinner.visibility = View.GONE
        binding.quoteText.visibility = View.VISIBLE
        binding.quoteText.text = randomQuote
    }
}
