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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.timestampformatter.TimestampFormatter
import com.weatherapp.core.ToastUtils
import com.weatherapp.core.roundDoubleToIntMin
import com.weatherapp.core.ui.theme.AppFont
import com.weatherapp.domain.models.ForecastResponse
import com.weatherapp.domain.models.List


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ForecastScreen(
    viewModel: ForecastViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var forecast by remember { mutableStateOf(ForecastResponse()) }


    // Observe weather Response
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
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        if (!isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
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
                            text = "Forecast",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = AppFont.MontserratFont,
                            fontWeight = FontWeight.Bold,
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
                text = TimestampFormatter.getDayNameFromTimestamp(forecast.dt!!.toLong())
                    .ifEmpty { "Sunday" },
                fontFamily = AppFont.MontserratFont,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = TimestampFormatter.convertTimestampToString(forecast.dt!!.toLong())
                    .ifEmpty { "1 am" },
                fontFamily = AppFont.MontserratFont,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "${roundDoubleToIntMin(forecast.main?.temp ?: 0.0)}°C",
                fontFamily = AppFont.MontserratFont,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary,
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