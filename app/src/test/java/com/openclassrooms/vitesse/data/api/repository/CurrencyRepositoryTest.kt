package com.openclassrooms.vitesse.data.api.repository

import com.openclassrooms.vitesse.data.api.network.CurrencyApi
import com.openclassrooms.vitesse.data.api.network.FallbackApi
import com.openclassrooms.vitesse.data.api.response.ResponseApiCurrency
import com.openclassrooms.vitesse.domain.model.PoundCurrency
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class CurrencyRepositoryTest {

    @Mock
    private lateinit var currencyMock: CurrencyApi

    @Mock
    private lateinit var fallbackMock: FallbackApi

    @Mock
    private lateinit var repository: CurrencyRepository

    @BeforeEach
    fun setUp() {
        repository = CurrencyRepository(currencyMock, fallbackMock)
    }

    @Test
    fun getPoundCurrencyNormalExecution()  = runTest {
        // Given
        val mockPoundCurrency = PoundCurrency("19/11/2024", 1.0)
        whenever(currencyMock.getCurrencies()).thenReturn(ResponseApiCurrency("19/11/2024", mapOf("gbp" to 1.0)))

        // When
        val result = repository.getPoundCurrency()

        // Then
        assertEquals(mockPoundCurrency, result)
    }

    @Test
    fun getPoundCurrencyFallbackExecution()  = runTest {
        // Given
        val mockPoundCurrency = PoundCurrency("19/11/2024", 1.0)
        whenever(currencyMock.getCurrencies()).thenThrow(RuntimeException())
        whenever(fallbackMock.getCurrencies()).thenReturn(ResponseApiCurrency("19/11/2024", mapOf("gbp" to 1.0)))

        // When
        val result = repository.getPoundCurrency()

        // Then
        assertEquals(mockPoundCurrency, result)
    }

}