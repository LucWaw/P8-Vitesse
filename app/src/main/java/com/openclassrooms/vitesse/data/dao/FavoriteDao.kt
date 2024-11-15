package com.openclassrooms.vitesse.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.FavoriteDto
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Upsert
    suspend fun addFavorite(favorite: FavoriteDto): Long


    @Query("SELECT candidateId FROM favorite")
    fun getFavoriteIds(): Flow<List<Long>>

    @Query("SELECT * FROM favorite WHERE candidateId = :id")
    suspend fun getFavoriteById(id: Long): FavoriteDto


    @Query("SELECT * FROM candidate where id = :candidateId")
    suspend fun getCandidateFromFavoriteId(candidateId: Long): CandidateDto

    @Query("SELECT * FROM candidate WHERE id IN (:candidateIds)")
    fun getCandidatesByIds(candidateIds: List<Long>): Flow<List<CandidateDto>>

    @Query("DELETE FROM favorite WHERE candidateId = :candidateId")
    suspend fun deleteFavoriteById(candidateId: Long)
}