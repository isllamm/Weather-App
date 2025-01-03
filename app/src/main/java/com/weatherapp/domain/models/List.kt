package com.weatherapp.domain.models

import com.google.gson.annotations.SerializedName

data class List(
    @SerializedName("dt") var dt: Int? = null,
    @SerializedName("main") var main: Main? = Main(),
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
)
