package com.example.weathersnap.presentation.navigation

sealed class Screen(val route: String) {
    data object Weather : Screen("weather")
    data object CreateReport : Screen("create_report")
    data object Camera : Screen("camera")
    data object SavedReports : Screen("saved_reports")
}

