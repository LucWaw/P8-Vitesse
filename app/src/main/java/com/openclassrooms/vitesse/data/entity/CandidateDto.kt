package com.openclassrooms.vitesse.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

@Entity(tableName = "candidate")
data class CandidateDto(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val firstName : String,
    val lastName : String,
    val phoneNumber : Int,
    val email : String,
    val birthday : Long,
    val salaryClaim : Double,
    val notes : String,
    val image : URI
)