package com.weatherapp.domain.repo

import com.weatherapp.core.Resource
import com.weatherapp.domain.models.City
import com.weatherapp.domain.models.CurrentWeatherResponse
import com.weatherapp.domain.models.ForecastResponse
import kotlinx.coroutines.flow.Flow


interface Repo {
    suspend fun getCurrentWeather(city: String): Resource<CurrentWeatherResponse>
    suspend fun get5DaysForecast(city: String): ForecastResponse
    suspend fun getCityLocal(): Flow<List<City>>
    suspend fun insertCityToLocal(city: City)
}