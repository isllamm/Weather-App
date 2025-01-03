package com.weatherapp.domain.usecase


import com.weatherapp.domain.models.City
import com.weatherapp.domain.repo.Repo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetLocalCityUseCaseTest {

    private lateinit var getLocalCityUseCase: GetLocalCityUseCase
    private val repo: Repo = mockk()

    @Before
    fun setUp() {
        getLocalCityUseCase = GetLocalCityUseCase(repo)
    }

    @Test
    fun `invoke returns list of cities from repo`() = runTest {
        val mockCities = listOf(City(name = "Cairo"), City(name = "Alexandria"))
        coEvery { repo.getCityLocal() } returns flowOf(mockCities)

        val result = getLocalCityUseCase().toList()

        assertEquals(1, result.size) // Since it's a single emission
        assertEquals(mockCities, result.first())
    }

    @Test
    fun `invoke handles empty city list`() = runTest {
        coEvery { repo.getCityLocal() } returns flowOf(emptyList())

        val result = getLocalCityUseCase().toList()

        assertEquals(1, result.size)
        assertEquals(emptyList<City>(), result.first())
    }
}
