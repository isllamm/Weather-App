package com.weatherapp.domain.usecase

import com.weatherapp.domain.models.City
import com.weatherapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetLocalCityUseCase
@Inject
constructor(
    private val repo: Repo
) {
    suspend operator fun invoke(): Flow<List<City>> {
        return repo.getCityLocal()
    }
}