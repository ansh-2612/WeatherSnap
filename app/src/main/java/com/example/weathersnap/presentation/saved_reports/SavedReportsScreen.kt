package com.example.weathersnap.presentation.saved_reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SavedReportsScreen(
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
            text = "Saved Reports",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "No reports saved yet.",
            modifier = Modifier.padding(top = 16.dp)
        )

        OutlinedButton(
            onClick = onBackClick,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text("Back")
        }
    }
}

