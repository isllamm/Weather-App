package com.weatherapp.data.repo

import com.weatherapp.data.local.CitiesDb
import com.weatherapp.data.remote.ApiService
import com.weatherapp.domain.repo.Repo

class RepoImpl(private val api: ApiService,private val db: CitiesDb) : Repo {

}