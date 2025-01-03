package com.weatherapp.presentation.features.forecastscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherapp.domain.usecase.ForecastUseCase
import com.weatherapp.domain.usecase.GetLocalCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ForecastViewModel @Inject constructor(
    val getLocalCityUseCase: GetLocalCityUseCase,
    private val forecastUseCase: ForecastUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ForecastState>(ForecastState.Loading)
    val state: StateFlow<ForecastState> = _state.asStateFlow()
    var cityNameFromLocal by mutableStateOf("")

    private fun processIntent(intent: ForecastIntent) {
        when (intent) {
            is ForecastIntent.LoadForecast -> fetchForecast(intent.city)
        }
    }

    private fun fetchForecast(city: String) {
        viewModelScope.launch {
            _state.value = ForecastState.Loading
            try {
                val response = forecastUseCase(city)
                if (response.cod == "200") {
                    _state.value = ForecastState.Success(response)
                } else {
                    _state.value = ForecastState.Error("API Error")
                }
            } catch (e: Exception) {
                _state.value = ForecastState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    init {
        getLocalCity()
    }

    private fun getLocalCity() = viewModelScope.launch {
        getLocalCityUseCase().collect {
            if (it.isNotEmpty()) {
                cityNameFromLocal = it.last().name
            }
            processIntent(ForecastIntent.LoadForecast(cityNameFromLocal.ifEmpty { "Cairo" }))
        }
    }


}