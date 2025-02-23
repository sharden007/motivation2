package com.example.quote3.utils

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ShareUtils {

    /**
     * Captures a screenshot of the given activity and returns the file.
     */
    fun captureScreenshot(activity: Activity): File? {
        return try {
            // Get root view of the activity
            val rootView = activity.window.decorView.rootView

            // Enable drawing cache and capture bitmap
            rootView.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(rootView.drawingCache)
            rootView.isDrawingCacheEnabled = false

            // Save bitmap to a file in the external cache directory
            val file = File(activity.externalCacheDir, "screenshot.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Shares the given file via an intent.
     */
    fun shareScreenshot(activity: Activity, file: File) {
        try {
            // Get URI for the file using FileProvider
            val uri: Uri = FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.provider",
                file
            )

            // Create share intent
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/png" // MIME type for images
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission to other apps
            }

            // Start share activity
            activity.startActivity(Intent.createChooser(intent, "Share Screenshot"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
