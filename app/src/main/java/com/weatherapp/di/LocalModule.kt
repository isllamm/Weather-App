package com.weatherapp.di

import android.app.Application
import androidx.room.Room
import com.weatherapp.data.local.CitiesDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun providesAppDb(app: Application): CitiesDb =
        Room.databaseBuilder(app, CitiesDb::class.java, "cities_db")
            .fallbackToDestructiveMigration()
            .build()

}