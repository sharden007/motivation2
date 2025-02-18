
package com.example.inspirationalquotes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dbHelper = DatabaseHelper(application)

    // LiveData to hold the current quote
    private val _quote = MutableLiveData<String>()
    val quote: LiveData<String> get() = _quote

    /**
     * Loads a random quote from the database asynchronously.
     */
    fun loadRandomQuote() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch a random quote from the database
                val randomQuote = dbHelper.getRandomQuote()

                // Post the result back to LiveData on the main thread
                withContext(Dispatchers.Main) {
                    _quote.value = randomQuote ?: "No inspirational quotes available."
                }
            } catch (e: Exception) {
                // Handle any exceptions gracefully
                withContext(Dispatchers.Main) {
                    _quote.value = "Error fetching quote. Please try again."
                }
            }
        }
    }
}
