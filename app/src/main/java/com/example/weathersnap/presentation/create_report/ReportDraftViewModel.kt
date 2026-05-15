package com.example.weathersnap.presentation.create_report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun setWeatherSnapshot(weatherInfo: WeatherInfo) {
        _uiState.value = _uiState.value.copy(
            weatherInfo = weatherInfo
        )
    }

    fun onNotesChange(notes: String) {
        _uiState.value = _uiState.value.copy(
            notes = notes
        )
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
                    clearDraft()
                    onSuccess()
                }
                .onFailure { error ->
                    onError(error.message ?: "Unable to save report")
                }
        }
    }

    fun clearDraft() {
        _uiState.value = ReportDraftUiState()
    }
}