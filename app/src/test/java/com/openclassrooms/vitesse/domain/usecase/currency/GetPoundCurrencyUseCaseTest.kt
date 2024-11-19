package com.openclassrooms.vitesse.domain.usecase.currency

import com.openclassrooms.vitesse.data.api.repository.CurrencyRepository
import com.openclassrooms.vitesse.domain.model.PoundCurrency
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class GetPoundCurrencyUseCaseTest {

    @Mock
    private lateinit var currencyRepository: CurrencyRepository


    private lateinit var getPoundCurrencyUseCase: GetPoundCurrencyUseCase

    @BeforeEach
    fun setUp() {
        getPoundCurrencyUseCase = GetPoundCurrencyUseCase(currencyRepository)
    }

    @Test
    fun `test when get pound currency, should return data from currencyApi`() = runTest {
        // Given
        val expectedCurrency = PoundCurrency("zddz", 1.23)

        // Simulate successful response from currencyApi

        whenever(currencyRepository.getPoundCurrency()).thenReturn(expectedCurrency)

        // Act
        val result = getPoundCurrencyUseCase.execute()

        // Assert
        assertEquals(expectedCurrency, result)
    }


}
