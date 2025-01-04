package com.weatherapp.presentation.navgraph

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weatherapp.presentation.features.forecastscreen.ForecastScreen
import com.weatherapp.presentation.features.weatherscreen.WeatherScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavGraph(
    navController: NavHostController,
    startDestination: String = Routes.WEATHER,
    onToggleTheme: () -> Unit,
    onChangeTabBarStatus: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.WEATHER) {
            onChangeTabBarStatus(true)
            WeatherScreen(onToggleTheme = onToggleTheme)
        }
        composable(Routes.FORECAST) {
            onChangeTabBarStatus(true)
            ForecastScreen()
        }
    }
}

