package com.weatherapp.presentation.features.weatherscreen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.weatherapp.core.Resource
import com.weatherapp.core.ToastUtils
import com.weatherapp.core.ui.theme.AppFont
import com.weatherapp.core.ui.theme.DarkBlue
import com.weatherapp.core.ui.theme.LightBlue
import com.weatherapp.core.ui.theme.backgroundColor
import com.weatherapp.domain.models.CurrentWeatherResponse
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun WeatherScreen(
    navController: NavController = rememberNavController(),
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    var weather by remember { mutableStateOf(CurrentWeatherResponse()) }
    val weatherState by viewModel.weatherFlow.collectAsState()
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
                .background(backgroundColor),
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
                    Text(
                        text = viewModel.cityNameFromLocal.ifEmpty { "Cairo" },
                        fontSize = 20.sp,
                        fontFamily = AppFont.MontserratFont,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location"
                    )
                }
                Spacer(modifier = Modifier.height(54.dp))

                Row (
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "${weather.main?.temp?.roundToInt()}",
                        textAlign = TextAlign.Center,
                        fontFamily = AppFont.MontserratFont,
                        fontWeight = FontWeight.Bold,
                        fontSize = 60.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "\u00B0",
                        textAlign = TextAlign.Center,
                        fontFamily = AppFont.MontserratFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 60.sp,
                        color = Color.Black
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
                        text = weather.weather[0].description?:"",
                        fontSize = 18.sp,
                        color = LightBlue,
                        fontFamily = AppFont.MontserratFont,
                        fontWeight = FontWeight.Medium,
                        style = TextStyle(
                            fontFeatureSettings = "c2sc, smcp"
                        )
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "${roundDoubleToIntMin(weather.main?.tempMax?:0.0)}° / ${roundDoubleToIntMin(weather.main?.tempMin?:0.0)}° Feels like ${roundDoubleToIntMin(weather.main?.feelsLike?:0.0)}°",
                    fontSize = 18.sp,
                    color = DarkBlue,
                    fontFamily = AppFont.MontserratFont,
                    fontWeight = FontWeight.Normal,
                )
            }

        }
        if (isLoading) CircularProgressIndicator(modifier = Modifier.size(80.dp))
    }

}
fun roundDoubleToIntMin(doubleValue: Double): Int {
    return floor(doubleValue).toInt()
}
fun roundDoubleToIntMax(doubleValue: Double): Int {
    return ceil(doubleValue).toInt()
}