package com.weatherapp.presentation.features.weatherscreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.core.Resource
import com.weatherapp.domain.models.City
import com.weatherapp.domain.models.CurrentWeatherResponse
import com.weatherapp.domain.usecase.CurrentWeatherUseCase
import com.weatherapp.domain.usecase.GetLocalCityUseCase
import com.weatherapp.domain.usecase.InsertLocalCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject
constructor(
    val getLocalCityUseCase: GetLocalCityUseCase,
    private val insertLocalCityUseCase: InsertLocalCityUseCase,
    private val weatherUseCase: CurrentWeatherUseCase,
) : ViewModel() {
    private val _weatherFlow = MutableStateFlow<Resource<CurrentWeatherResponse>>(Resource.Idle())
    val weatherFlow = _weatherFlow.asStateFlow()

    var cityNameFromLocal by mutableStateOf("")

    companion object {
        private const val TAG = "WeatherViewModel"
    }

    init {
        getLocalCity()
    }

    private fun getLocalCity() = viewModelScope.launch {
        getLocalCityUseCase().collect {
            if (it.isNotEmpty()) {
                cityNameFromLocal = it.last().name
            }
            getCurrentWeather(cityNameFromLocal.ifEmpty { "Cairo" })
        }
    }

    fun insertCity(city: City) = viewModelScope.launch {
        try {
            insertLocalCityUseCase(city)
        } catch (e: Exception) {
            Log.d(TAG, "insertAddress: ${e.message}")
        }
    }.invokeOnCompletion {
        getLocalCity()
    }

    private fun getCurrentWeather(cityName: String) = viewModelScope.launch {
        _weatherFlow.emit(Resource.Loading())
        _weatherFlow.emit(weatherUseCase(city = cityName))
    }
}