package com.example.quote3

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.quote3.databinding.ActivityMainBinding
import com.example.quote3.utils.ShareUtils
import java.util.Calendar
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

        // Request notification permission for Android 13+ (API level 33)
        requestNotificationPermission()

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
     * Requests the POST_NOTIFICATIONS runtime permission for Android 13+.
     */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001 // Request code for notification permission
                )
            }
        }
    }

    /**
     * Sets a random gradient background from a predefined list of gradients.
     */
    private fun setRandomBackground() {
        val gradients = arrayOf(
            R.drawable.background_gradient_1,
            R.drawable.background_gradient_2,
            R.drawable.background_gradient_3,
            R.drawable.background_gradient_4
        )

        val randomIndex = Random.nextInt(gradients.size)
        binding.rootLayout.setBackgroundResource(gradients[randomIndex])
    }

    /**
     * Fetches a random quote from the database and updates the UI.
     */
    private fun loadRandomQuote() {
        binding.loadingSpinner.visibility = View.VISIBLE
        binding.quoteText.visibility = View.GONE

        val randomQuote = dbHelper.getRandomQuote() ?: "No inspirational quotes available."

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
            set(Calendar.HOUR_OF_DAY, 16) // Set desired hour (e.g., 3 PM)
            set(Calendar.MINUTE, 35)
            set(Calendar.SECOND, 0)
        }

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
