package com.weatherapp.domain.usecase


import com.weatherapp.domain.models.City
import com.weatherapp.domain.repo.Repo
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class InsertLocalCityUseCaseTest {

    private lateinit var insertLocalCityUseCase: InsertLocalCityUseCase
    private val repo: Repo = mockk(relaxed = true)

    @Before
    fun setUp() {
        insertLocalCityUseCase = InsertLocalCityUseCase(repo)
    }

    @Test
    fun `invoke calls insertCityToLocal on repo`() = runTest {
        val city = City(name = "Cairo")

        insertLocalCityUseCase(city)

        coVerify { repo.insertCityToLocal(city) }
    }
}
