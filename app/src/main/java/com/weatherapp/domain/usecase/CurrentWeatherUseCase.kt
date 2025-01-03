package com.weatherapp.domain.usecase

import com.weatherapp.core.AppError
import com.weatherapp.core.Resource
import com.weatherapp.domain.models.CurrentWeatherResponse
import com.weatherapp.domain.repo.Repo
import javax.inject.Inject

class CurrentWeatherUseCase
@Inject
constructor(
    private val repo: Repo
) {
    suspend operator fun invoke(city: String): Resource<CurrentWeatherResponse> {
        return when (val result = repo.getCurrentWeather(city)) {
            is Resource.Success -> {
                if (result.data.cod==200) {
                    Resource.Success(result.data)
                } else {
                    Resource.Error(AppError.ApiError("API Error"))
                }
            }

            is Resource.Error -> Resource.Error(result.error)
            is Resource.Loading -> Resource.Loading()
            is Resource.Idle -> Resource.Idle()
        }
    }
}