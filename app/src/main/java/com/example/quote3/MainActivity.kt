package com.example.quote3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.example.quote3.DatabaseHelper
import com.example.quote3.databinding.ActivityMainBinding
import com.example.quote3.utils.ShareUtils
import java.util.Calendar
import android.view.View
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Declare binding property
    private lateinit var dbHelper: DatabaseHelper // Declare DatabaseHelper property

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding and set the content view
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize DatabaseHelper
        dbHelper = DatabaseHelper(this)

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

        // Set up click listener for the share button
        binding.shareButtonIcon.setOnClickListener {
            val screenshot = ShareUtils.captureScreenshot(this) // Capture screenshot
            if (screenshot != null) {
                ShareUtils.shareScreenshot(this, screenshot) // Share screenshot
            } else {
                println("Error capturing screenshot") // Log error (optional)
            }
        }

        // Schedule the QuoteWorker to send a daily notification with a random quote
        scheduleDailyQuoteWorker()
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

    /**
     * Schedules the QuoteWorker to run once daily at a specific time.
     */
    private fun scheduleDailyQuoteWorker() {
        val currentTime = Calendar.getInstance()
        val targetTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 9) // Set desired hour (e.g., 9 AM)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        // Calculate initial delay in milliseconds
        var delay = targetTime.timeInMillis - currentTime.timeInMillis
        if (delay < 0) {
            delay += TimeUnit.DAYS.toMillis(1) // Schedule for the next day if time has passed
        }

        val dailyWorkRequest = OneTimeWorkRequestBuilder<QuoteWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(dailyWorkRequest)
    }
}