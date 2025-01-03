package com.weatherapp.presentation.features.forecastscreen

import com.weatherapp.domain.models.ForecastResponse

sealed class ForecastState {
    data object Loading : ForecastState()
    data class Success(val forecast: ForecastResponse) : ForecastState()
    data class Error(val message: String) : ForecastState()
}
