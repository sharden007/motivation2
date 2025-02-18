package com.example.quote3

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class SwipeGestureListener(context: Context, private val onSwipeLeft: () -> Unit) :
    View.OnTouchListener {

    private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        fun handleSwipe(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 != null && e2 != null && e1.x - e2.x > 100) {
                onSwipeLeft()
                return true
            }
            return false
        }
    })

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        // Ensure event is non-null before passing it to gestureDetector
        return event?.let { gestureDetector.onTouchEvent(it) } ?: false
    }
}
