package com.openclassrooms.vitesse.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.domain.model.Candidate
import kotlinx.coroutines.flow.first

class CandidateRepository(private val candidateDao: CandidateDao) {

    // Retrieve all candidates
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllCandidates(): List<Candidate> {
        return try {
            candidateDao.getAllCandidates()
                .first() // Collect the first emission from the Flow
                .map { Candidate.fromDto(it) } // Convert each DTO to a Candidate
        } catch (e: Exception) {
            Log.d("DatabaseError", "Error while retrieving candidates", e)
            emptyList()
        }
    }

    // Add a new candidate
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun addCandidate(candidate: Candidate) {
        try {
            candidateDao.addCandidate(candidate.toDto())
        } catch (e: Exception) {
            Log.d("DatabaseError", "Error while adding a candidate", e)
        }
    }

    // Retrieve a candidate by ID
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getCandidateById(id: Long): Candidate? {
        return try {
            candidateDao.getCandidateById(id).let { Candidate.fromDto(it) }
        } catch (e: Exception) {
            Log.d("DatabaseError", "Error while retrieving a candidate by ID", e)
            null
        }
    }



    // Delete a candidate
    suspend fun deleteCandidate(candidate: Candidate) {
        try {
            candidate.id?.let {
                candidateDao.deleteCandidateById(it)
            } ?: throw IllegalArgumentException("Candidate ID cannot be null")
        } catch (e: Exception) {
            Log.d("DatabaseError", "Error while deleting a candidate", e)
        }
    }
}

