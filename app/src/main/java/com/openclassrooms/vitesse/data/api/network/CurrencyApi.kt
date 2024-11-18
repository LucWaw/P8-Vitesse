package com.openclassrooms.vitesse.data.api.network

import com.openclassrooms.vitesse.data.api.response.ResponseApiCurrency
import retrofit2.http.GET

interface CurrencyApi {
    @GET("v1/currencies/eur.json")
    suspend fun getCurrencies(): ResponseApiCurrency
}