package com.weatherapp.domain.models

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("main") var main: Main? = Main(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("cod") var cod: Int? = null
)
