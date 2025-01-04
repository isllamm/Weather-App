package com.weatherapp.presentation.features.weatherscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.weatherapp.core.PrefsHelper
import com.weatherapp.core.Resource
import com.weatherapp.core.ToastUtils
import com.weatherapp.core.roundDoubleToIntMin
import com.weatherapp.core.ui.theme.AppFont
import com.weatherapp.domain.models.CurrentWeatherResponse
import com.weatherapp.presentation.features.addcitybottomsheet.AddCityBottomSheet
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    onToggleTheme: () -> Unit,
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var showSheet by remember { mutableStateOf(false) }
    var weather by remember { mutableStateOf(CurrentWeatherResponse()) }
    val weatherState by viewModel.weatherFlow.collectAsState()


    if (showSheet) {
        AddCityBottomSheet(
            onDismiss = {
                showSheet = false
            },
            onApply = { cityName ->
                if (cityName.isNotEmpty()) {
                    viewModel.isCityAvailable(cityName)
                    showSheet = false
                }
            }
        )
    }
    // Observe weather Response
    LaunchedEffect(key1 = weatherState) {
        when (val state = weatherState) {
            is Resource.Error -> {
                ToastUtils.showToast(context, state.error.message.toString())
                isLoading = false
            }

            is Resource.Idle -> {}
            is Resource.Loading -> isLoading = true
            is Resource.Success -> {
                isLoading = false
                weather = state.data
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (!isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = viewModel.cityNameFromLocal.ifEmpty { "Cairo" },
                        fontSize = 20.sp,
                        fontFamily = AppFont.MontserratFont,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            showSheet = true
                        },
                        color = MaterialTheme.colorScheme.secondary,
                        style = TextStyle(
                            fontFeatureSettings = " smcp"
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.clickable {
                            showSheet = true
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (PrefsHelper.isDarkMode()) {
                        Text(
                            "☀️",
                            fontSize = 32.sp,
                            modifier = Modifier.clickable {
                                PrefsHelper.setIsDarkMode(false)
                                onToggleTheme()
                            })
                    }
                    if (!PrefsHelper.isDarkMode()) {
                        Text(
                            "🌙",
                            fontSize = 32.sp,
                            modifier = Modifier.clickable {
                                PrefsHelper.setIsDarkMode(true)
                                onToggleTheme()
                            })
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                }
                Spacer(modifier = Modifier.height(60.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${weather.main?.temp?.roundToInt()}",
                        textAlign = TextAlign.Center,
                        fontFamily = AppFont.MontserratFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 60.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "\u00B0",
                        textAlign = TextAlign.Center,
                        fontFamily = AppFont.MontserratFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 60.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    if (weather.weather.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data("https://openweathermap.org/img/wn/${weather.weather[0].icon}@4x.png")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Weather Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (weather.weather.isNotEmpty()) {
                    Text(
                        text = weather.weather[0].description ?: "",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = AppFont.MontserratFont,
                        fontWeight = FontWeight.Medium,
                        style = TextStyle(
                            fontFeatureSettings = "c2sc, smcp"
                        )
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "${roundDoubleToIntMin(weather.main?.tempMax ?: 0.0)}° / ${
                        roundDoubleToIntMin(
                            weather.main?.tempMin ?: 0.0
                        )
                    }° Feels like ${roundDoubleToIntMin(weather.main?.feelsLike ?: 0.0)}°",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = AppFont.MontserratFont,
                    fontWeight = FontWeight.Normal,
                )
            }

        }
        if (isLoading) CircularProgressIndicator(modifier = Modifier.size(80.dp))
    }

}
