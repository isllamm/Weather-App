package com.weatherapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "City")
data class City(
    @PrimaryKey
    val id: String,
    val name: String,
)
