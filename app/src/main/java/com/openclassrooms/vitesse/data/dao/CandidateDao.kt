package com.openclassrooms.vitesse.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.vitesse.data.entity.CandidateDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CandidateDao {
    @Insert
    suspend fun addCandidate(Candidate: CandidateDto): Long


    @Query("SELECT * FROM candidate")
    fun getAllCandidates(): Flow<List<CandidateDto>>

    @Query("SELECT * FROM candidate WHERE id = :id")
    suspend fun getCandidateById(id: Long): CandidateDto

    @Query("DELETE FROM candidate WHERE id = :id")
    suspend fun deleteCandidateById(id: Long)

    @Query("DELETE FROM candidate")
    fun deleteAll()

}