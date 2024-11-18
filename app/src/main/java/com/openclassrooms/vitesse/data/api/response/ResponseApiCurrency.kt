package com.openclassrooms.vitesse.data.api.response

import com.openclassrooms.vitesse.domain.model.PoundCurrency
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseApiCurrency(
    @Json(name = "date")
    val date: String,
    @Json(name = "eur") val eurData: Map<String, Double>
) {
    fun toDomainModel(): PoundCurrency {
        val gbpValue = eurData["gbp"] ?: throw IllegalArgumentException("GBP not found in response")
        return PoundCurrency(date, gbpValue)
    }
}
