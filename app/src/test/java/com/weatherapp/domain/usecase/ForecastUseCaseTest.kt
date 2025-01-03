package com.weatherapp.domain.usecase


import com.weatherapp.domain.models.ForecastResponse
import com.weatherapp.domain.repo.Repo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ForecastUseCaseTest {

    private lateinit var forecastUseCase: ForecastUseCase
    private val repo: Repo = mockk()

    @Before
    fun setUp() {
        forecastUseCase = ForecastUseCase(repo)
    }

    @Test
    fun `invoke returns forecast response from repo`() = runBlocking {
        val mockResponse = ForecastResponse()
        coEvery { repo.get5DaysForecast("Cairo") } returns mockResponse

        val result = forecastUseCase("Cairo")

        assertEquals(mockResponse, result)
    }

    @Test
    fun `invoke handles repo exception`() = runBlocking {
        val exception = RuntimeException("Network error")
        coEvery { repo.get5DaysForecast("ErrorCity") } throws exception

        try {
            forecastUseCase("ErrorCity")
            assert(false) // This line should not execute
        } catch (e: Exception) {
            assertEquals(exception, e)
        }
    }
}
