package com.example.weathersnap.presentation.saved_reports

import androidx.lifecycle.ViewModel
import com.example.weathersnap.data.local.ReportEntity
import com.example.weathersnap.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SavedReportsViewModel @Inject constructor(
    repository: WeatherRepository
) : ViewModel() {

    val savedReports: Flow<List<ReportEntity>> = repository.getSavedReports()
}

