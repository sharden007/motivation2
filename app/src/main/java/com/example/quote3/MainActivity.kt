package com.example.inspirationalquotes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application)
    val quote = MutableLiveData<String?>()

    fun loadRandomQuote() {
        quote.postValue(dbHelper.getRandomQuote())
    }
}
