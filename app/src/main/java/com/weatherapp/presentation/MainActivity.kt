package com.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.weatherapp.core.PrefsHelper
import com.weatherapp.core.ui.theme.WeatherAppTheme
import com.weatherapp.presentation.features.mainscreen.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(PrefsHelper.isDarkMode()) }
            WeatherAppTheme(darkTheme = isDarkTheme) {
                MainScreen(
                    onToggleTheme = {
                        isDarkTheme = !isDarkTheme
                    }
                )
            }
        }
    }
}

