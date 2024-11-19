package com.openclassrooms.vitesse.domain.usecase.favorite

import com.openclassrooms.vitesse.data.repository.FavoriteRepository
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(private val favoriteRepository: FavoriteRepository) {
    suspend fun execute(favoriteId: Long) {
        favoriteRepository.deleteFavoriteById(favoriteId)
    }
}