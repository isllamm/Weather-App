package com.weatherapp.data.repo

import com.weatherapp.core.safeApiCall
import com.weatherapp.data.local.CitiesDb
import com.weatherapp.data.remote.ApiService
import com.weatherapp.domain.models.City
import com.weatherapp.domain.repo.Repo

class RepoImpl(private val api: ApiService, private val db: CitiesDb) : Repo {
    override suspend fun getCurrentWeather(city: String) =
        safeApiCall { api.getCurrentWeather(city = city) }
    override suspend fun get5DaysForecast(city: String) =
        api.get5DayForecast(city = city)
    override suspend fun getCityLocal() = db.citiesDao().getCity()
    override suspend fun insertCityToLocal(city: City) =
        db.citiesDao().insertCity(city)
}