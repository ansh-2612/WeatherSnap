package com.example.weathersnap.presentation.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.data.repository.WeatherRepository
import com.example.weathersnap.domain.model.CitySuggestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onCityQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(
            cityQuery = query,
            selectedCity = null,
            suggestionError = null
        )

        searchJob?.cancel()

        if (query.trim().length <= 2) {
            _uiState.value = _uiState.value.copy(
                suggestions = emptyList(),
                isLoadingSuggestions = false
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(400)

            _uiState.value = _uiState.value.copy(
                isLoadingSuggestions = true,
                suggestionError = null
            )

            val result = repository.searchCities(query)

            result
                .onSuccess { cities ->
                    _uiState.value = _uiState.value.copy(
                        suggestions = cities,
                        isLoadingSuggestions = false,
                        suggestionError = if (cities.isEmpty()) "No cities found" else null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        suggestions = emptyList(),
                        isLoadingSuggestions = false,
                        suggestionError = error.message ?: "Something went wrong"
                    )
                }
        }
    }

    fun onCitySelected(city: CitySuggestion) {
        _uiState.value = _uiState.value.copy(
            selectedCity = city,
            cityQuery = city.displayName,
            suggestions = emptyList(),
            suggestionError = null,
            isLoadingSuggestions = false
        )
    }
}