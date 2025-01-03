package com.weatherapp.presentation.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.weatherapp.presentation.features.weatherscreen.WeatherScreen


@Composable
fun HomeNavGraph(
    navController: NavHostController,
    startDestination: String = Routes.WEATHER,
    onChangeTabBarStatus: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.WEATHER) {
            onChangeTabBarStatus(true)
            WeatherScreen(navController = navController)
        }
    }
}

