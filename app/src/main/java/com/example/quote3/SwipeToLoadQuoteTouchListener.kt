package com.example.quote3

import kotlin.math.abs
import android.view.MotionEvent
import android.view.View

class SwipeToLoadQuoteTouchListener(
    private val onSwipeLeft: () -> Unit
) : View.OnTouchListener {

    private var startX: Float = 0f
    private var startY: Float = 0f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                val endX = event.x
                val endY = event.y
                val deltaX = startX - endX
                val deltaY = abs(startY - endY)

                if (deltaX > SWIPE_THRESHOLD && deltaY < SWIPE_VERTICAL_THRESHOLD) {
                    v.performClick() // Notify accessibility services
                    onSwipeLeft()
                    return true
                }
            }
        }
        return false // Let other touch events propagate if not handled
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VERTICAL_THRESHOLD = 50
    }
}