package com.example.weathersnap.presentation.weather

import com.example.weathersnap.domain.model.CitySuggestion

data class WeatherUiState(
    val cityQuery: String = "",
    val suggestions: List<CitySuggestion> = emptyList(),
    val isLoadingSuggestions: Boolean = false,
    val suggestionError: String? = null,
    val selectedCity: CitySuggestion? = null
)

