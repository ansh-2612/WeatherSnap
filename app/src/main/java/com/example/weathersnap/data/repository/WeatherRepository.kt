package com.example.weathersnap.data.repository

import com.example.weathersnap.data.local.ReportDao
import com.example.weathersnap.data.local.ReportEntity
import com.example.weathersnap.data.remote.WeatherApiService
import com.example.weathersnap.domain.model.CitySuggestion
import com.example.weathersnap.domain.model.WeatherInfo
import javax.inject.Inject
import javax.inject.Singleton
import com.example.weathersnap.data.local.DraftReportEntity

@Singleton
class WeatherRepository @Inject constructor(
    private val apiService: WeatherApiService,
    private val reportDao: ReportDao
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

    suspend fun getCurrentWeather(city: CitySuggestion): Result<WeatherInfo> {
        return try {
            val response = apiService.getCurrentWeather(
                latitude = city.latitude,
                longitude = city.longitude
            )

            val current = response.current
                ?: return Result.failure(Exception("Weather data not available"))

            val weatherInfo = WeatherInfo(
                cityName = city.name,
                location = listOfNotNull(city.state, city.country).joinToString(", "),
                latitude = city.latitude,
                longitude = city.longitude,
                temperature = current.temperature ?: 0.0,
                condition = getWeatherCondition(current.weatherCode),
                humidity = current.humidity ?: 0,
                windSpeed = current.windSpeed ?: 0.0,
                pressure = current.pressure ?: 0.0
            )

            Result.success(weatherInfo)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveReport(
        weatherInfo: WeatherInfo,
        notes: String,
        imagePath: String?,
        originalImageSize: Long?,
        compressedImageSize: Long?
    ): Result<Unit> {
        return try {
            val report = ReportEntity(
                cityName = weatherInfo.cityName,
                location = weatherInfo.location,
                temperature = weatherInfo.temperature,
                condition = weatherInfo.condition,
                humidity = weatherInfo.humidity,
                windSpeed = weatherInfo.windSpeed,
                pressure = weatherInfo.pressure,
                notes = notes,
                imagePath = imagePath,
                originalImageSize = originalImageSize,
                compressedImageSize = compressedImageSize,
                savedAt = System.currentTimeMillis()
            )

            reportDao.insertReport(report)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSavedReports() = reportDao.getAllReports()
    suspend fun saveDraft(draft: DraftReportEntity) {
        reportDao.upsertDraft(draft)
    }

    suspend fun getDraftOnce(): DraftReportEntity? {
        return reportDao.getDraftOnce()
    }

    suspend fun clearDraft() {
        reportDao.clearDraft()
    }


    private fun getWeatherCondition(code: Int?): String {
        return when (code) {
            0 -> "Clear Sky"
            1, 2 -> "Partly Cloudy"
            3 -> "Cloudy"
            45, 48 -> "Foggy"
            51, 53, 55 -> "Drizzle"
            61, 63, 65 -> "Rain"
            71, 73, 75 -> "Snow"
            80, 81, 82 -> "Rain Showers"
            95, 96, 99 -> "Thunderstorm"
            else -> "Unknown"
        }
    }
}