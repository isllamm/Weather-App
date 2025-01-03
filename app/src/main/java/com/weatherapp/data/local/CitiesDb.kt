package com.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weatherapp.domain.models.City

@Database([City::class], version = 1)
abstract class CitiesDb : RoomDatabase() {
    abstract fun citiesDao(): CitiesDao
}