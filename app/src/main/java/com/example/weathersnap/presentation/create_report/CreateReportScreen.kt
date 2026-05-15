package com.example.weathersnap.presentation.create_report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import coil.compose.AsyncImage
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File


@Composable
fun CreateReportScreen(
    draftState: ReportDraftUiState,
    onNotesChange: (String) -> Unit,
    onCapturePhotoClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val weather = draftState.weatherInfo

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4FAFF))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Create Report",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B4F8A)
            )

            Text(
                text = "Save a weather snapshot with photo and notes",
                fontSize = 14.sp,
                color = Color(0xFF5B6B7A),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            if (weather == null) {
                Text(
                    text = "No weather selected. Please go back and select a city first.",
                    color = Color(0xFFD32F2F)
                )
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF0984E3),
                                        Color(0xFF0B65C2)
                                    )
                                )
                            )
                            .padding(18.dp)
                    ) {
                        Column {
                            Text(
                                text = weather.cityName,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = weather.location,
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "${weather.temperature.toInt()}°C • ${weather.condition}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            HorizontalDivider(
                                color = Color.White.copy(alpha = 0.35f)
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "Humidity: ${weather.humidity}%   Wind: ${weather.windSpeed.toInt()} km/h   Pressure: ${weather.pressure.toInt()} hPa",
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Photo Preview",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF263238)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (draftState.imagePath != null) {
                        AsyncImage(
                            model = File(draftState.imagePath),
                            contentDescription = "Captured weather photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "No photo captured yet",
                            color = Color(0xFF607D8B)
                        )
                    }
                }
            }

            if (draftState.imagePath != null) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Original size: ${formatFileSize(draftState.originalImageSize)}",
                    fontSize = 12.sp,
                    color = Color(0xFF607D8B)
                )

                Text(
                    text = "Compressed size: ${formatFileSize(draftState.compressedImageSize)}",
                    fontSize = 12.sp,
                    color = Color(0xFF607D8B)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCapturePhotoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0878D8)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.padding(4.dp))

                Text("Capture Photo")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = draftState.notes,
                onValueChange = onNotesChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text("Write notes about current weather...")
                },
                shape = RoundedCornerShape(18.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSaveClick,
                enabled = weather != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0B7D53)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.padding(4.dp))

                Text("Save Report")
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Back")
            }
        }
    }
}

private fun formatFileSize(size: Long?): String {
    if (size == null || size <= 0L) return "Not available"

    val kb = size / 1024.0
    val mb = kb / 1024.0

    return if (mb >= 1) {
        String.format("%.2f MB", mb)
    } else {
        String.format("%.2f KB", kb)
    }
}