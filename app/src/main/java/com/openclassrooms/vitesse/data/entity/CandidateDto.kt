package com.openclassrooms.vitesse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidate")
data class CandidateDto(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val firstName : String,
    val lastName : String,
    val phoneNumber : String,
    val email : String,
    val birthday : Long,
    val salaryClaim : Double,
    val notes : String,
    val image : String
)