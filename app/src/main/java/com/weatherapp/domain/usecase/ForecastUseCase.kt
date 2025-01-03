package com.weatherapp.domain.usecase

import com.weatherapp.domain.models.ForecastResponse
import com.weatherapp.domain.repo.Repo
import javax.inject.Inject

class ForecastUseCase
@Inject
constructor(
    private val repo: Repo
) {
    suspend operator fun invoke(city: String): ForecastResponse {
        return repo.get5DaysForecast(city)
    }
}