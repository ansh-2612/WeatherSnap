package com.example.weathersnap.presentation.create_report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateReportScreen(
    onCapturePhotoClick: () -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Report",
            style = MaterialTheme.typography.headlineLarge
        )

        Button(
            onClick = onCapturePhotoClick,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text("Capture Photo")
        }

        Button(
            onClick = onSaveClick,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Save Report")
        }

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Back")
        }
    }
}

