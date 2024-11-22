package com.openclassrooms.vitesse.data.persistence.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite",
    foreignKeys = [
        ForeignKey(
            entity = CandidateDto::class,
            parentColumns = ["id"],
            childColumns = ["candidateId"],
            onDelete = ForeignKey.CASCADE
            // to automatically delete favorite if the candidate is deleted
        )
    ]
)
data class FavoriteDto(
    @PrimaryKey val candidateId: Long
    // candidateId Is Both primary key and Foreign Key (so its also unique)
)