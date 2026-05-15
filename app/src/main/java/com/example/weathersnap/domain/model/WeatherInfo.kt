package com.example.weathersnap.domain.model
data class WeatherInfo(
    val cityName: String,
    val location: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Double
)