package com.openclassrooms.vitesse.domain.model

data class PoundCurrency(
    val date: String, // Date de la réponse
    val gbpValue: Double // Taux de conversion EUR vers GBP
)
