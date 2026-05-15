package com.example.weathersnap.domain.model

data class CitySuggestion(
    val id: Long,
    val name: String,
    val state: String?,
    val country: String?,
    val latitude: Double,
    val longitude: Double
) {
    val displayName: String
        get() = listOfNotNull(name, state, country).joinToString(", ")
}

