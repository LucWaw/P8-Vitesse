package com.openclassrooms.vitesse.data.repository

import android.util.Log
import com.openclassrooms.vitesse.data.dao.FavoriteDao
import com.openclassrooms.vitesse.domain.model.Candidate
import com.openclassrooms.vitesse.domain.model.Favorite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    // Retrieve all favorite candidates
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getAllFavoritesCandidates(): List<Candidate> {
        return try {
            // Retrieve favorite candidate ids
            favoriteDao.getFavoriteIds()
                .flatMapLatest { favoriteIds ->
                    // Retrieve candidates using the ids of favorites
                    favoriteDao.getCandidatesByIds(favoriteIds)
                }
                .first() // Collect the first value emitted by the Flow
                .map { Candidate.fromDto(it) } // Convert each CandidateDto to a Candidate object
        } catch (e: Exception) {
            Log.d("DatabaseError", "Error while retrieving favorites", e)
            emptyList()
        }
    }

    // Add a favorite
    suspend fun addFavorite(favorite: Favorite) {
        try {
            favoriteDao.addFavorite(favorite.toDto())
        } catch (e: Exception) {
            Log.d("DatabaseError", "Error while adding favorite", e)
        }
    }

    // Get a single favorite by candidate ID
    suspend fun getFavoriteById(candidateId: Long): Favorite? {
        return try {
            val favoriteDto = favoriteDao.getFavoriteById(candidateId)
            Favorite.fromDto(favoriteDto)
        } catch (e: Exception) {
            Log.d("DatabaseError", "Candidate is not a favorite")
            null
        }
    }

    // Delete a favorite by candidate ID
    suspend fun deleteFavoriteById(candidateId: Long) {
        try {
            favoriteDao.deleteFavoriteById(candidateId)
        } catch (e: Exception) {
            Log.d("DatabaseError", "Error while deleting favorite by ID", e)
        }
    }
}