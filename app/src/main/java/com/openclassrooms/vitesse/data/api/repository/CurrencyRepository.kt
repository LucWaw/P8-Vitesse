package com.openclassrooms.vitesse.data.api.repository

import com.openclassrooms.vitesse.data.api.network.CurrencyApi
import com.openclassrooms.vitesse.data.api.network.FallbackApi
import com.openclassrooms.vitesse.domain.model.PoundCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyApi: CurrencyApi,
    private val fallbackApi: FallbackApi
) {
    suspend fun getPoundCurrency(): PoundCurrency {
        return withContext(Dispatchers.IO) {
            try {
                // Principal request
                currencyApi.getCurrencies().toDomainModel()
            } catch (e: Exception) {
                // In case of failure, fallback request
                fallbackApi.getCurrencies().toDomainModel()
            }
        }
    }
}
