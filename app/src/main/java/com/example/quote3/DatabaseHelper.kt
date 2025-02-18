package com.example.quote3

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "motivational_quotes.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_QUOTES = "quotes"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $TABLE_QUOTES (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                quote_text TEXT NOT NULL
            )
        """
        db?.execSQL(createTableQuery)

        // Insert sample data
        db?.execSQL("INSERT INTO $TABLE_QUOTES (quote_text) VALUES ('Believe in yourself.')")
        db?.execSQL("INSERT INTO $TABLE_QUOTES (quote_text) VALUES ('You are stronger than you think.')")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_QUOTES")
        onCreate(db)
    }

    // Method to fetch a random quote from the database
    fun getRandomQuote(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM quotes ORDER BY RANDOM() LIMIT 1", null)
    }
}
