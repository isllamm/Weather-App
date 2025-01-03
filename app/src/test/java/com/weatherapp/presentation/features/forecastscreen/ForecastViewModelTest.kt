package com.weatherapp.presentation.features.forecastscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.weatherapp.core.MainCoroutineRule
import com.weatherapp.domain.models.City
import com.weatherapp.domain.models.ForecastResponse
import com.weatherapp.domain.usecase.ForecastUseCase
import com.weatherapp.domain.usecase.GetLocalCityUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class ForecastViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ForecastViewModel

    private val getLocalCityUseCase: GetLocalCityUseCase = mockk()
    private val forecastUseCase: ForecastUseCase = mockk()

    @Before
    fun setup() {
        viewModel = ForecastViewModel(
            getLocalCityUseCase,
            forecastUseCase
        )
    }

    @Test
    fun `getLocalCity sets cityNameFromLocal and triggers LoadForecast intent`() = runTest {
        val mockCities = listOf(City(name = "Cairo"))
        coEvery { getLocalCityUseCase() } returns flowOf(mockCities)
        coEvery { forecastUseCase("Cairo") } returns ForecastResponse(cod = "200")

        viewModel.getLocalCity()

        assertEquals("Cairo", viewModel.cityNameFromLocal)
        val state = viewModel.state.value
        assert(state is ForecastState.Success)
    }

    @Test
    fun `fetchForecast updates state to Success when response is valid`() = runTest {
        val mockResponse = ForecastResponse(cod = "200")
        coEvery { forecastUseCase("London") } returns mockResponse

        viewModel.fetchForecast("London")

        val state = viewModel.state.value
        assert(state is ForecastState.Success)
        assertEquals(mockResponse, (state as ForecastState.Success).forecast)
    }

    @Test
    fun `fetchForecast updates state to Error when response is invalid`() = runTest {
        val mockResponse = ForecastResponse(cod = "404")
        coEvery { forecastUseCase("UnknownCity") } returns mockResponse

        viewModel.fetchForecast("UnknownCity")

        val state = viewModel.state.value
        assert(state is ForecastState.Error)
        assertEquals("API Error", (state as ForecastState.Error).message)
    }

    @Test
    fun `fetchForecast handles exceptions and updates state to Error`() = runTest {
        coEvery { forecastUseCase("ErrorCity") } throws Exception("Network Error")

        viewModel.fetchForecast("ErrorCity")

        val state = viewModel.state.value
        assert(state is ForecastState.Error)
        assertEquals("Network Error", (state as ForecastState.Error).message)
    }

}

