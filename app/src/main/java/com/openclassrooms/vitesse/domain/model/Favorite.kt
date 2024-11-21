package com.openclassrooms.vitesse.domain.model

import com.openclassrooms.vitesse.data.persistence.entity.FavoriteDto

data class Favorite(
    val candidateId: Long
) {
    // Convert this Favorite to a FavoriteDto
    fun toDto(): FavoriteDto {
        return FavoriteDto(
            candidateId = candidateId)
    }

    companion object {
        // Convert a FavoriteDto to a Favorite
        fun fromDto(favoriteDto: FavoriteDto): Favorite {
            return Favorite(
                candidateId = favoriteDto.candidateId
            )
        }
    }
}

