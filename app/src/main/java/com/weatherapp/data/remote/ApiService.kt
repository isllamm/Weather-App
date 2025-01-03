package com.weatherapp.data.remote

import com.weatherapp.core.Constants
import com.weatherapp.domain.models.CurrentWeatherResponse
import com.weatherapp.domain.models.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = "metric"
    ): CurrentWeatherResponse

    @GET("forecast")
    suspend fun get5DayForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = "metric"
    ): ForecastResponse
}