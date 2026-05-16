package com.example.weathersnap.presentation.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weathersnap.domain.model.CitySuggestion
import com.example.weathersnap.domain.model.WeatherInfo

@Composable
fun WeatherScreen(
    onCreateReportClick: (WeatherInfo) -> Unit,
    onReportsClick: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4FAFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "WeatherSnap",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B4F8A)
            )

            Text(
                text = "Search live weather and create local reports",
                fontSize = 14.sp,
                color = Color(0xFF5B6B7A),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            OutlinedTextField(
                value = uiState.cityQuery,
                onValueChange = viewModel::onCityQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Search city",
                        color = Color(0xFF78909C)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Color(0xFF0B4F8A)
                    )
                },
                trailingIcon = {
                    if (uiState.cityQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color(0xFF0B4F8A),
                            modifier = Modifier.clickable {
                                viewModel.onCityQueryChange("")
                            }
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF102A43),
                    unfocusedTextColor = Color(0xFF102A43),
                    cursorColor = Color(0xFF0878D8),
                    focusedBorderColor = Color(0xFF0878D8),
                    unfocusedBorderColor = Color(0xFFB0BEC5),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            Text(
                text = "Enter more than 2 letters to start city suggestions.",
                fontSize = 12.sp,
                color = Color(0xFF3478B8),
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )

            AnimatedVisibility(
                visible = uiState.isLoadingSuggestions
            ) {
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Searching cities...",
                        fontSize = 13.sp,
                        color = Color(0xFF5B6B7A)
                    )
                }
            }

            AnimatedVisibility(
                visible = uiState.suggestions.isNotEmpty()
            ) {
                SuggestionsCard(
                    suggestions = uiState.suggestions,
                    onSuggestionClick = viewModel::onCitySelected
                )
            }

            AnimatedVisibility(
                visible = uiState.suggestionError != null
            ) {
                Text(
                    text = uiState.suggestionError.orEmpty(),
                    color = Color(0xFFD32F2F),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            WeatherInfoCard(
                weatherInfo = uiState.weatherInfo,
                selectedCity = uiState.selectedCity,
                isLoading = uiState.isLoadingWeather,
                error = uiState.weatherError
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    uiState.weatherInfo?.let { weatherInfo ->
                        onCreateReportClick(weatherInfo)
                    }
                },
                enabled = uiState.weatherInfo != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0878D8),
                    disabledContainerColor = Color(0xFFB7D4EA)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Create Report",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onReportsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Article,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Reports",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun SuggestionsCard(
    suggestions: List<CitySuggestion>,
    onSuggestionClick: (CitySuggestion) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            suggestions.forEach { suggestion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSuggestionClick(suggestion)
                        }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF607D8B)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = suggestion.displayName,
                        fontSize = 14.sp,
                        color = Color(0xFF263238)
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherInfoCard(
    weatherInfo: WeatherInfo?,
    selectedCity: CitySuggestion?,
    isLoading: Boolean,
    error: String?
) {
    val cityName = weatherInfo?.cityName ?: selectedCity?.name ?: "Select a city"

    val location = weatherInfo?.location
        ?: selectedCity?.let {
            listOfNotNull(it.state, it.country).joinToString(", ")
        }
        ?: "Weather details will appear here"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF0984E3),
                            Color(0xFF0B65C2)
                        )
                    )
                )
                .padding(22.dp)
        ) {
            Column {
                Text(
                    text = cityName,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = location,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(18.dp))

                when {
                    isLoading -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 32.dp)
                        ) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(28.dp),
                                strokeWidth = 3.dp
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Loading weather...",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }
                    }

                    error != null -> {
                        Text(
                            text = error,
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(vertical = 32.dp)
                        )
                    }

                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = weatherInfo?.let {
                                        "${it.temperature.toInt()}°C"
                                    } ?: "--°C",
                                    fontSize = 52.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )

                                Text(
                                    text = weatherInfo?.condition ?: "Weather not loaded yet",
                                    fontSize = 16.sp,
                                    color = Color.White
                                )

                                Text(
                                    text = if (weatherInfo != null) {
                                        "Live Open-Meteo data"
                                    } else {
                                        "Select a suggestion first"
                                    },
                                    fontSize = 13.sp,
                                    color = Color.White.copy(alpha = 0.85f),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }

                            Text(
                                text = getWeatherEmoji(weatherInfo?.condition),
                                fontSize = 70.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        HorizontalDivider(
                            color = Color.White.copy(alpha = 0.35f)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            WeatherStatItem(
                                icon = Icons.Default.WaterDrop,
                                label = "Humidity",
                                value = weatherInfo?.let {
                                    "${it.humidity}%"
                                } ?: "--%"
                            )

                            WeatherStatItem(
                                icon = Icons.Default.Air,
                                label = "Wind Speed",
                                value = weatherInfo?.let {
                                    "${it.windSpeed.toInt()} km/h"
                                } ?: "-- km/h"
                            )

                            WeatherStatItem(
                                icon = Icons.Default.Speed,
                                label = "Pressure",
                                value = weatherInfo?.let {
                                    "${it.pressure.toInt()} hPa"
                                } ?: "-- hPa"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherStatItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.85f)
        )

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

private fun getWeatherEmoji(condition: String?): String {
    return when (condition) {
        "Clear Sky" -> "☀️"
        "Partly Cloudy" -> "⛅"
        "Cloudy" -> "☁️"
        "Foggy" -> "🌫️"
        "Drizzle" -> "🌦️"
        "Rain" -> "🌧️"
        "Rain Showers" -> "🌦️"
        "Snow" -> "❄️"
        "Thunderstorm" -> "⛈️"
        else -> "⛅"
    }
}