package com.openclassrooms.vitesse.domain.usecase.currency

import com.openclassrooms.vitesse.data.api.repository.CurrencyRepository
import com.openclassrooms.vitesse.domain.model.PoundCurrency
import javax.inject.Inject

class GetPoundCurrencyUseCase @Inject constructor(private val currencyRepository: CurrencyRepository) {
    suspend fun execute() : PoundCurrency {
        return currencyRepository.getPoundCurrency()
    }
}