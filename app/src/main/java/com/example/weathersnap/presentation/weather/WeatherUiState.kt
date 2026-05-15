package com.example.weathersnap.presentation.weather

import com.example.weathersnap.domain.model.CitySuggestion
import com.example.weathersnap.domain.model.WeatherInfo

data class WeatherUiState(
    val cityQuery: String = "",
    val suggestions: List<CitySuggestion> = emptyList(),
    val isLoadingSuggestions: Boolean = false,
    val suggestionError: String? = null,

    val selectedCity: CitySuggestion? = null,

    val weatherInfo: WeatherInfo? = null,
    val isLoadingWeather: Boolean = false,
    val weatherError: String? = null
)