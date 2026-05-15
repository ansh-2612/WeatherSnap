package com.example.weathersnap.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "draft_report")
data class DraftReportEntity(
    @PrimaryKey
    val id: Int = 1,

    val cityName: String?,
    val location: String?,
    val temperature: Double?,
    val condition: String?,
    val humidity: Int?,
    val windSpeed: Double?,
    val pressure: Double?,

    val notes: String = "",
    val imagePath: String?,
    val originalImageSize: Long?,
    val compressedImageSize: Long?,

    val updatedAt: Long
)

