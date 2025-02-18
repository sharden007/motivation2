package com.example.inspirationalquotes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "motivational_quotes", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create the quotes table
        db.execSQL(
            """CREATE TABLE quotes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                text TEXT NOT NULL,
                category TEXT
            )"""
        )

        // Insert sample data into the quotes table
        db.execSQL("INSERT INTO quotes (text, category) VALUES ('Believe in yourself!', 'Motivation')")
        db.execSQL("INSERT INTO quotes (text, category) VALUES ('Stay positive, work hard, make it happen.', 'Inspiration')")
        db.execSQL("INSERT INTO quotes (text, category) VALUES ('Success is not final, failure is not fatal: It is the courage to continue that counts.', 'Success')")
        db.execSQL("INSERT INTO quotes (text, category) VALUES ('Dream big and dare to fail.', 'Dream')")
        db.execSQL("INSERT INTO quotes (text, category) VALUES ('Act as if what you do makes a difference. It does.', 'Action')")

        Log.d("DatabaseHelper", "Sample data inserted into the database.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the old table and create a new one
        db.execSQL("DROP TABLE IF EXISTS quotes")
        onCreate(db)
    }

    fun getRandomQuote(): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT text FROM quotes ORDER BY RANDOM() LIMIT 1", null)
        var quote: String? = null

        if (cursor.moveToFirst()) {
            quote = cursor.getString(0)
            Log.d("DatabaseHelper", "Fetched Quote: $quote")
        } else {
            Log.d("DatabaseHelper", "No quotes found in the database.")
            quote = "No inspirational quotes available."
        }

        cursor.close()
        return quote
    }
}
