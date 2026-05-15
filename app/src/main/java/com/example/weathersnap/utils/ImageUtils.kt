package com.example.weathersnap.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

data class CompressedImageResult(
    val compressedPath: String,
    val originalSize: Long,
    val compressedSize: Long
)

fun createImageFile(context: Context): File {
    val imagesDir = File(context.filesDir, "weather_images")
    if (!imagesDir.exists()) {
        imagesDir.mkdirs()
    }

    return File(
        imagesDir,
        "weather_${System.currentTimeMillis()}.jpg"
    )
}

fun compressImage(
    context: Context,
    originalFile: File
): CompressedImageResult {
    val originalSize = originalFile.length()

    val bitmap = BitmapFactory.decodeFile(originalFile.absolutePath)

    val compressedFile = File(
        File(context.filesDir, "weather_images"),
        "compressed_${System.currentTimeMillis()}.jpg"
    )

    FileOutputStream(compressedFile).use { outputStream ->
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            60,
            outputStream
        )
    }

    val compressedSize = compressedFile.length()

    return CompressedImageResult(
        compressedPath = compressedFile.absolutePath,
        originalSize = originalSize,
        compressedSize = compressedSize
    )
}

