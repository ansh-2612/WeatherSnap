package com.example.weathersnap.presentation.create_report

import com.example.weathersnap.domain.model.WeatherInfo

data class ReportDraftUiState(
    val weatherInfo: WeatherInfo? = null,
    val notes: String = "",
    val imagePath: String? = null,
    val originalImageSize: Long? = null,
    val compressedImageSize: Long? = null
)

