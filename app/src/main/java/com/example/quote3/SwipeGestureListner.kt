package com.example.quote3

import android.view.GestureDetector
import android.view.MotionEvent

abstract class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 != null && e2 != null) {
            val deltaX = e2.x - e1.x
            if (deltaX > 100) { // Detect swipe right gesture (adjust threshold as needed)
                onSwipeLeft()
                return true
            }
        }
        return false
    }

    abstract fun onSwipeLeft()
}
