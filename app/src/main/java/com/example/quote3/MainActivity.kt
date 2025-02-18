package com.example.quote3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.inspirationalquotes.DatabaseHelper // Import DatabaseHelper from its correct package

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize DatabaseHelper and use it
        val dbHelper = DatabaseHelper(this)
        val randomQuote = dbHelper.getRandomQuote()
        println("Random Quote: $randomQuote")
    }
}
