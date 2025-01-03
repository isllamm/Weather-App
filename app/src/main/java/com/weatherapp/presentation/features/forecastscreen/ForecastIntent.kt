package com.weatherapp.presentation.features.forecastscreen

sealed class ForecastIntent {
    data class LoadForecast(val city: String) : ForecastIntent()
}