package com.openclassrooms.vitesse.domain.usecase.favorite

import com.openclassrooms.vitesse.data.persistence.repository.FavoriteRepository
import com.openclassrooms.vitesse.domain.model.Favorite
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(private val favoriteRepository: FavoriteRepository) {
    suspend fun execute(favorite : Favorite){
        favoriteRepository.addFavorite(favorite)
    }
}