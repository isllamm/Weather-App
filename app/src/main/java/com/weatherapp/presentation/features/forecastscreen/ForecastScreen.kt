package com.weatherapp.presentation.features.forecastscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.weatherapp.core.ToastUtils
import com.weatherapp.core.roundDoubleToIntMin
import com.weatherapp.core.ui.theme.AppFont
import com.weatherapp.core.ui.theme.DarkBlue
import com.weatherapp.core.ui.theme.backgroundColor
import com.weatherapp.domain.models.ForecastResponse
import com.weatherapp.domain.models.List
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var forecast by remember { mutableStateOf(ForecastResponse()) }
    // Trigger LoadForecast intent
    LaunchedEffect(Unit) {
        viewModel.processIntent(ForecastIntent.LoadForecast("Cairo"))
    }
    when (state) {
        is ForecastState.Loading -> isLoading = true
        is ForecastState.Success -> {
            isLoading = false
            forecast = (state as ForecastState.Success).forecast
        }

        is ForecastState.Error -> {
            val errorMessage = (state as ForecastState.Error).message
            isLoading = false
            ToastUtils.showToast(context, errorMessage)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        if (!isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
            ) {
                item {
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
                    Spacer(modifier = Modifier.height(16.dp))
                }
                itemsIndexed(forecast.list) { _, day ->
                    ForecastCard(day)
                    HorizontalDivider(color = Color.White, thickness = 1.dp)
                }
                item {
                    Spacer(modifier = Modifier.height(120.dp))
                }
            }
        } else {
            CircularProgressIndicator(Modifier.size(80.dp))
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastCard(
    forecast: List = List(),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = getDayNameFromTimestamp(forecast.dt!!.toLong()).ifEmpty { "Sunday" },
                fontFamily = AppFont.MontserratFont,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = DarkBlue,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = convertTimestampToString(forecast.dt!!.toLong()).ifEmpty { "Sunday" },
                fontFamily = AppFont.MontserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = DarkBlue,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${roundDoubleToIntMin(forecast.main?.temp ?: 0.0)}°C",
                fontFamily = AppFont.MontserratFont,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = DarkBlue,
                modifier = Modifier.weight(1f)
            )

            if (forecast.weather.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Weather Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .weight(1f)
                        .size(36.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getDayNameFromTimestamp(timestamp: Long): String {
    val instant = Instant.ofEpochSecond(timestamp)
    val formatter = DateTimeFormatter.ofPattern("EEEE") // EEEE for full day name (e.g., Monday)
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}

fun convertTimestampToString(timestamp: Long): String {
    val date = Date(timestamp * 1000) // Convert seconds to milliseconds if needed
    val dateFormat = SimpleDateFormat("h a", Locale.getDefault())
    val formattedDate = dateFormat.format(date)

    return formattedDate
}