package com.weatherapp.domain.usecase

import com.weatherapp.domain.models.City
import com.weatherapp.domain.repo.Repo
import javax.inject.Inject


class InsertLocalCityUseCase
@Inject
constructor(
    private val repo: Repo
) {
    suspend operator fun invoke(city: City): Any {
        return repo.insertCityToLocal(city)
    }
}