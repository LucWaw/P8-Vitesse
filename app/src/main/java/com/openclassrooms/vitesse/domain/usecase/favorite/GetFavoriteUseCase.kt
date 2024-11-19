package com.openclassrooms.vitesse.domain.usecase.favorite

import com.openclassrooms.vitesse.data.repository.FavoriteRepository
import com.openclassrooms.vitesse.domain.model.Favorite
import javax.inject.Inject

class GetFavoriteUseCase @Inject constructor(private val favoriteRepository: FavoriteRepository) {
    suspend fun execute(favoriteId: Long) : Favorite? {
        return favoriteRepository.getFavoriteById(favoriteId)
    }
}