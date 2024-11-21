package com.openclassrooms.vitesse.domain.usecase.favorite

import com.openclassrooms.vitesse.data.persistence.repository.FavoriteRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import javax.inject.Inject

class GetAllFavoriteCandidateUseCase @Inject constructor(private val favoriteRepository: FavoriteRepository) {
    suspend fun execute() : List<Candidate> {
        return favoriteRepository.getAllFavoritesCandidates()
    }
}