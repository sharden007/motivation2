package com.example.quote3

import android.database.Cursor
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var quoteTextView: TextView
    private lateinit var instructionTextView: TextView
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        quoteTextView = findViewById(R.id.quoteTextView)
        instructionTextView = findViewById(R.id.instructionTextView)

        // Set instruction text
        instructionTextView.text =
            "Swipe left on the quote to load the next quote. All inspirational quotes provided by Lori Jones-Harden."

        // Initialize database helper
        dbHelper = DatabaseHelper(this)

        // Load initial random quote
        loadRandomQuote()

        // Gesture detector for swipe functionality using SwipeGestureListener
        gestureDetector = GestureDetector(this, object : SwipeGestureListener() {
            override fun onSwipeLeft() {
                loadRandomQuote()
            }
        })
    }

    private fun loadRandomQuote() {
        val cursor: Cursor? = dbHelper.getRandomQuote()
        if (cursor != null && cursor.moveToFirst()) {
            val quote = cursor.getString(cursor.getColumnIndexOrThrow("quote_text"))
            quoteTextView.text = quote
            cursor.close()
        } else {
            quoteTextView.text = "No quotes available."
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
}
