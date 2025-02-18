package com.example.inspirationalquotes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "motivational_quotes", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE quotes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                text TEXT NOT NULL,
                category TEXT
            )"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS quotes")
        onCreate(db)
    }

    fun getRandomQuote(): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT text FROM quotes ORDER BY RANDOM() LIMIT 1", null)
        var quote: String? = null
        if (cursor.moveToFirst()) {
            quote = cursor.getString(0)
        }
        cursor.close()
        return quote
    }
}
