package com.example.weathersnap.presentation.create_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.data.local.DraftReportEntity
import com.example.weathersnap.data.repository.WeatherRepository
import com.example.weathersnap.domain.model.WeatherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportDraftViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportDraftUiState())
    val uiState: StateFlow<ReportDraftUiState> = _uiState.asStateFlow()

    init {
        restoreDraftIfAvailable()
    }

    fun setWeatherSnapshot(weatherInfo: WeatherInfo) {
        _uiState.value = ReportDraftUiState(
            weatherInfo = weatherInfo
        )
        persistDraft()
    }

    fun onNotesChange(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
        persistDraft()
    }

    fun setCapturedImage(
        imagePath: String,
        originalSize: Long,
        compressedSize: Long
    ) {
        _uiState.value = _uiState.value.copy(
            imagePath = imagePath,
            originalImageSize = originalSize,
            compressedImageSize = compressedSize
        )
        persistDraft()
    }

    fun saveReport(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentState = _uiState.value
        val weather = currentState.weatherInfo

        if (weather == null) {
            onError("Weather snapshot missing")
            return
        }

        viewModelScope.launch {
            val result = repository.saveReport(
                weatherInfo = weather,
                notes = currentState.notes,
                imagePath = currentState.imagePath,
                originalImageSize = currentState.originalImageSize,
                compressedImageSize = currentState.compressedImageSize
            )

            result
                .onSuccess {
                    repository.clearDraft()
                    clearDraft()
                    onSuccess()
                }
                .onFailure { error ->
                    onError(error.message ?: "Unable to save report")
                }
        }
    }

    fun discardDraft() {
        viewModelScope.launch {
            repository.clearDraft()
            clearDraft()
        }
    }

    private fun persistDraft() {
        val state = _uiState.value
        val weather = state.weatherInfo ?: return

        viewModelScope.launch {
            repository.saveDraft(
                DraftReportEntity(
                    cityName = weather.cityName,
                    location = weather.location,
                    temperature = weather.temperature,
                    condition = weather.condition,
                    humidity = weather.humidity,
                    windSpeed = weather.windSpeed,
                    pressure = weather.pressure,
                    notes = state.notes,
                    imagePath = state.imagePath,
                    originalImageSize = state.originalImageSize,
                    compressedImageSize = state.compressedImageSize,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    private fun restoreDraftIfAvailable() {
        viewModelScope.launch {
            val draft = repository.getDraftOnce() ?: return@launch

            val weatherInfo = WeatherInfo(
                cityName = draft.cityName ?: return@launch,
                location = draft.location.orEmpty(),
                latitude = 0.0,
                longitude = 0.0,
                temperature = draft.temperature ?: 0.0,
                condition = draft.condition.orEmpty(),
                humidity = draft.humidity ?: 0,
                windSpeed = draft.windSpeed ?: 0.0,
                pressure = draft.pressure ?: 0.0
            )

            _uiState.value = ReportDraftUiState(
                weatherInfo = weatherInfo,
                notes = draft.notes,
                imagePath = draft.imagePath,
                originalImageSize = draft.originalImageSize,
                compressedImageSize = draft.compressedImageSize,
                isDraftRestored = true
            )
        }
    }

    private fun clearDraft() {
        _uiState.value = ReportDraftUiState()
    }
}