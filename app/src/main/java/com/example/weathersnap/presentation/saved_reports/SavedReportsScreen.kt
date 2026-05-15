package com.example.weathersnap.presentation.saved_reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weathersnap.data.local.ReportEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedReportsScreen(
    onBackClick: () -> Unit,
    viewModel: SavedReportsViewModel = hiltViewModel()
) {
    val reports by viewModel.savedReports.collectAsState(initial = emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4FAFF))
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = {
                    Text(
                        text = "Saved Reports",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF4FAFF),
                    titleContentColor = Color(0xFF0B4F8A),
                    navigationIconContentColor = Color(0xFF0B4F8A)
                )
            )

            if (reports.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No reports saved yet.",
                        color = Color(0xFF607D8B),
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(reports) { report ->
                        SavedReportCard(report = report)
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SavedReportCard(
    report: ReportEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = report.cityName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B4F8A)
            )

            Text(
                text = report.location,
                fontSize = 13.sp,
                color = Color(0xFF607D8B)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "${report.temperature.toInt()}°C • ${report.condition}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238)
            )

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReportStatRow(
                    iconText = "💧",
                    label = "Humidity",
                    value = "${report.humidity}%"
                )

                ReportStatRow(
                    iconText = "🌬️",
                    label = "Wind Speed",
                    value = "${report.windSpeed.toInt()} km/h"
                )

                ReportStatRow(
                    iconText = "⚙️",
                    label = "Pressure",
                    value = "${report.pressure.toInt()} hPa"
                )
            }

            if (report.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Notes",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF263238)
                )

                Text(
                    text = report.notes,
                    fontSize = 14.sp,
                    color = Color(0xFF455A64),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Original size: ${formatSize(report.originalImageSize)}",
                fontSize = 12.sp,
                color = Color(0xFF607D8B)
            )

            Text(
                text = "Compressed size: ${formatSize(report.compressedImageSize)}",
                fontSize = 12.sp,
                color = Color(0xFF607D8B)
            )

            Text(
                text = "Saved: ${formatTime(report.savedAt)}",
                fontSize = 12.sp,
                color = Color(0xFF607D8B),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun ReportStatRow(
    iconText: String,
    label: String,
    value: String
) {
    androidx.compose.foundation.layout.Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$iconText $label",
            color = Color(0xFF455A64),
            fontSize = 14.sp
        )

        Text(
            text = value,
            color = Color(0xFF263238),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatTime(time: Long): String {
    val formatter = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return formatter.format(Date(time))
}

private fun formatSize(size: Long?): String {
    if (size == null || size <= 0L) return "Not available"

    val kb = size / 1024.0
    val mb = kb / 1024.0

    return if (mb >= 1) {
        String.format(Locale.getDefault(), "%.2f MB", mb)
    } else {
        String.format(Locale.getDefault(), "%.2f KB", kb)
    }
}