package com.openclassrooms.vitesse.data.dao

import androidx.room.Query
import androidx.room.Upsert
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.FavoriteDto
import kotlinx.coroutines.flow.Flow

interface FavoriteDao {
    @Upsert
    suspend fun addFavorite(Favorite: FavoriteDto): Long


    @Query("SELECT candidateId FROM favorite")
    fun getFavoriteIds(): Flow<List<Long>>

    @Query("SELECT * FROM favorite WHERE candidateId = :id")
    suspend fun getFavoriteById(id: Long): FavoriteDto


    @Query("SELECT * FROM candidate where id = :candidateId")
    suspend fun getCandidateFromFavoriteId(candidateId: Long): CandidateDto

    @Query("SELECT * FROM candidate WHERE id IN (:candidateIds)")
    fun getCandidatesByIds(candidateIds: List<Long>): Flow<List<CandidateDto>>
}