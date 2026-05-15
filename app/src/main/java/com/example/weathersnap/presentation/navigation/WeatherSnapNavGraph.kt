package com.example.weathersnap.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathersnap.presentation.camera.CameraScreen
import com.example.weathersnap.presentation.create_report.CreateReportScreen
import com.example.weathersnap.presentation.create_report.ReportDraftViewModel
import com.example.weathersnap.presentation.saved_reports.SavedReportsScreen
import com.example.weathersnap.presentation.weather.WeatherScreen
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun WeatherSnapNavGraph() {
    val navController = rememberNavController()

    val reportDraftViewModel: ReportDraftViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Weather.route
    ) {
        composable(Screen.Weather.route) {
            WeatherScreen(
                onCreateReportClick = { weatherInfo ->
                    reportDraftViewModel.setWeatherSnapshot(weatherInfo)
                    navController.navigate(Screen.CreateReport.route)
                },
                onReportsClick = {
                    navController.navigate(Screen.SavedReports.route)
                }
            )
        }

        composable(Screen.CreateReport.route) {
            val draftState by reportDraftViewModel.uiState.collectAsState()

            CreateReportScreen(
                draftState = draftState,
                onNotesChange = reportDraftViewModel::onNotesChange,
                onCapturePhotoClick = {
                    navController.navigate(Screen.Camera.route)
                },
                onSaveClick = {
                    reportDraftViewModel.saveReport(
                        onSuccess = {
                            navController.navigate(Screen.SavedReports.route) {
                                popUpTo(Screen.Weather.route)
                            }
                        },
                        onError = {
                            // For now we ignore error. Later we can show Snackbar.
                        }
                    )
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