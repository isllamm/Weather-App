package com.weatherapp.presentation.features.weatherscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.weatherapp.core.MainCoroutineRule
import com.weatherapp.core.Resource
import com.weatherapp.domain.models.City
import com.weatherapp.domain.models.CurrentWeatherResponse
import com.weatherapp.domain.usecase.CurrentWeatherUseCase
import com.weatherapp.domain.usecase.GetLocalCityUseCase
import com.weatherapp.domain.usecase.InsertLocalCityUseCase
import io.mockk.Awaits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import kotlin.test.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: WeatherViewModel

    private val getLocalCityUseCase: GetLocalCityUseCase = mockk()
    private val insertLocalCityUseCase: InsertLocalCityUseCase = mockk(relaxed = true)
    private val weatherUseCase: CurrentWeatherUseCase = mockk()

    @Before
    fun setup() {
        viewModel = WeatherViewModel(
            getLocalCityUseCase = getLocalCityUseCase,
            insertLocalCityUseCase = insertLocalCityUseCase,
            weatherUseCase = weatherUseCase
        )
    }

    @Test
    fun `getLocalCity sets cityNameFromLocal and fetches weather for the city`() = runTest {
        val mockCityList = listOf(City(name = "Cairo"))
        coEvery { getLocalCityUseCase() } returns flowOf(mockCityList)
        coEvery { weatherUseCase(city = "Cairo") } returns Resource.Success(mockk(relaxed = true))

        viewModel.getLocalCity()

        assertEquals("Cairo", viewModel.cityNameFromLocal)

        viewModel.weatherFlow.test {
            assertEquals(Resource.Loading<CurrentWeatherResponse>(), awaitItem())
            assert(awaitItem() is Resource.Success<CurrentWeatherResponse>)
        }
    }

    @Test
    fun `getLocalCity fetches weather for default city when no local city is available`() =
        runTest {
            coEvery { getLocalCityUseCase() } returns flowOf(emptyList())
            coEvery { weatherUseCase(city = "Cairo") } returns Resource.Success(mockk(relaxed = true))

            viewModel.getLocalCity()

            assertEquals("", viewModel.cityNameFromLocal)

            viewModel.weatherFlow.test {
                assertEquals(Resource.Loading<CurrentWeatherResponse>(), awaitItem())
                assert(awaitItem() is Resource.Success<CurrentWeatherResponse>)
            }
        }

    @Test
    fun `insertCity calls insertLocalCityUseCase and refreshes local city`() = runTest {
        val mockCity = City(name = "New York")
        coEvery { insertLocalCityUseCase(mockCity) } just Awaits
        coEvery { getLocalCityUseCase() } returns flowOf(listOf(mockCity))

        viewModel.insertCity(mockCity)

        coVerify { insertLocalCityUseCase(mockCity) }
        assertEquals("New York", viewModel.cityNameFromLocal)
    }

    @Test
    fun `getCurrentWeather emits success response`() = runTest {
        val mockResponse = Resource.Success(mockk<CurrentWeatherResponse>(relaxed = true))
        coEvery { weatherUseCase(city = "London") } returns mockResponse

        viewModel.getCurrentWeather("London")

        viewModel.weatherFlow.test {
            assertEquals(Resource.Loading<CurrentWeatherResponse>(), awaitItem())
            assertEquals(mockResponse, awaitItem())
        }
    }

    @Test
    fun `getCurrentWeather emits error response`() = runTest {
        val mockError = Resource.Success(CurrentWeatherResponse())
        coEvery { weatherUseCase(city = "InvalidCity") } returns mockError

        viewModel.getCurrentWeather("InvalidCity")

        viewModel.weatherFlow.test {
            assertEquals(Resource.Loading<CurrentWeatherResponse>(), awaitItem())
            assertEquals(mockError, awaitItem())
        }
    }

    @Test
    fun `isCityAvailable inserts city when valid`() = runTest {
        val mockCity = "ValidCity"
        coEvery { weatherUseCase(city = mockCity) } returns Resource.Success(
            mockk<CurrentWeatherResponse>(
                relaxed = true
            )
        )

        viewModel.isCityAvailable(mockCity)

        coVerify { insertLocalCityUseCase(City(name = mockCity)) }
    }

    @Test
    fun `isCityAvailable emits error when invalid`() = runTest {
        val mockCity = "InvalidCity"
        val mockError = Resource.Success(CurrentWeatherResponse())
        coEvery { weatherUseCase(city = mockCity) } returns mockError

        viewModel.isCityAvailable(mockCity)

        viewModel.weatherFlow.test {
            assertEquals(mockError, awaitItem())
        }
    }
}
