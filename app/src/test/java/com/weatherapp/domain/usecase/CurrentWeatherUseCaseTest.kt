package com.weatherapp.domain.usecase


import com.weatherapp.core.AppError
import com.weatherapp.core.Resource
import com.weatherapp.domain.models.CurrentWeatherResponse
import com.weatherapp.domain.repo.Repo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrentWeatherUseCaseTest {

    private lateinit var currentWeatherUseCase: CurrentWeatherUseCase
    private val repo: Repo = mockk()

    @Before
    fun setUp() {
        currentWeatherUseCase = CurrentWeatherUseCase(repo)
    }

    @Test
    fun `invoke returns success when repo returns valid data with cod 200`() = runBlocking {
        val mockResponse = CurrentWeatherResponse(cod = 200)
        coEvery { repo.getCurrentWeather("Cairo") } returns Resource.Success(mockResponse)

        val result = currentWeatherUseCase("Cairo")

        assert(result is Resource.Success)
        assertEquals(mockResponse, (result as Resource.Success).data)
    }

    @Test
    fun `invoke returns error when repo returns success with invalid cod`() = runBlocking {
        val mockResponse = CurrentWeatherResponse(cod = 404)
        coEvery { repo.getCurrentWeather("InvalidCity") } returns Resource.Success(mockResponse)

        val result = currentWeatherUseCase("InvalidCity")

        assert(result is Resource.Error)
        assertEquals(AppError.ApiError("API Error"), (result as Resource.Error).error)
    }

    @Test
    fun `invoke returns error when repo returns Resource Error`() = runBlocking {
        val mockError = AppError.NetworkError("Network issue")
        coEvery { repo.getCurrentWeather("ErrorCity") } returns Resource.Error(mockError)

        val result = currentWeatherUseCase("ErrorCity")

        assert(result is Resource.Error)
        assertEquals(mockError, (result as Resource.Error).error)
    }

    @Test
    fun `invoke returns loading when repo returns Resource Loading`() = runBlocking {
        coEvery { repo.getCurrentWeather("LoadingCity") } returns Resource.Loading()

        val result = currentWeatherUseCase("LoadingCity")

        assert(result is Resource.Loading)
    }

    @Test
    fun `invoke returns idle when repo returns Resource Idle`() = runBlocking {
        coEvery { repo.getCurrentWeather("IdleCity") } returns Resource.Idle()

        val result = currentWeatherUseCase("IdleCity")

        assert(result is Resource.Idle)
    }
}
