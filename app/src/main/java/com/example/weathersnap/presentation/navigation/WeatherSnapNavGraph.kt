package com.example.weathersnap.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathersnap.presentation.camera.CameraScreen
import com.example.weathersnap.presentation.create_report.CreateReportScreen
import com.example.weathersnap.presentation.saved_reports.SavedReportsScreen
import com.example.weathersnap.presentation.weather.WeatherScreen

@Composable
fun WeatherSnapNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route
    ) {
        composable(Screen.Weather.route) {
            WeatherScreen(
                onCreateReportClick = {
                    navController.navigate(Screen.CreateReport.route)
                },
                onReportsClick = {
                    navController.navigate(Screen.SavedReports.route)
                }
            )
        }

        composable(Screen.CreateReport.route) {
            CreateReportScreen(
                onCapturePhotoClick = {
                    navController.navigate(Screen.Camera.route)
                },
                onSaveClick = {
                    navController.navigate(Screen.SavedReports.route) {
                        popUpTo(Screen.Weather.route)
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen(
                onCaptureClick = {
                    navController.popBackStack()
                },
                onCloseClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.SavedReports.route) {
            SavedReportsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

