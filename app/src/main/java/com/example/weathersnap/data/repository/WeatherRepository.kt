package com.example.weathersnap.data.repository

import com.example.weathersnap.data.remote.WeatherApiService
import com.example.weathersnap.domain.model.CitySuggestion
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val apiService: WeatherApiService
) {
    private val cityCache = mutableMapOf<String, List<CitySuggestion>>()

    suspend fun searchCities(query: String): Result<List<CitySuggestion>> {
        val cleanQuery = query.trim().lowercase()

        if (cleanQuery.length <= 2) {
            return Result.success(emptyList())
        }

        cityCache[cleanQuery]?.let { cachedCities ->
            return Result.success(cachedCities)
        }

        return try {
            val response = apiService.searchCities(query = cleanQuery)

            val cities = response.results.orEmpty().map { cityDto ->
                CitySuggestion(
                    id = cityDto.id,
                    name = cityDto.name,
                    state = cityDto.state,
                    country = cityDto.country,
                    latitude = cityDto.latitude,
                    longitude = cityDto.longitude
                )
            }

            cityCache[cleanQuery] = cities

            Result.success(cities)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

