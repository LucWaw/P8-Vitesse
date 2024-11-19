package com.openclassrooms.vitesse.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.openclassrooms.vitesse.data.entity.CandidateDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Upsert
    suspend fun addCandidate(Candidate: CandidateDto): Long

    @Query("SELECT * FROM candidate")
    fun getAllCandidates(): Flow<List<CandidateDto>>

    @Query("SELECT * FROM candidate WHERE id = :id")
    suspend fun getCandidateById(id: Long): CandidateDto

    @Query("DELETE FROM candidate WHERE id = :id")
    suspend fun deleteCandidateById(id: Long)

}