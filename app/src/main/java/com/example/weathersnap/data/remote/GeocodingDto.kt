package com.example.weathersnap.data.remote

import com.google.gson.annotations.SerializedName

data class GeocodingResponseDto(
    val results: List<CityDto>?
)

data class CityDto(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String?,
    @SerializedName("admin1")
    val state: String?
)

