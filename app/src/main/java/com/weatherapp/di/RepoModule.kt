package com.weatherapp.di


import com.weatherapp.data.local.CitiesDb
import com.weatherapp.data.remote.ApiService
import com.weatherapp.data.repo.RepoImpl
import com.weatherapp.domain.repo.Repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    fun provideMainRepo(api: ApiService, db: CitiesDb): Repo {
        return RepoImpl(api, db)
    }


}