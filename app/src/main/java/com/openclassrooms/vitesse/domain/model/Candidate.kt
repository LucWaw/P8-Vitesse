package com.openclassrooms.vitesse.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.openclassrooms.vitesse.data.entity.CandidateDto
import java.net.URI
import java.time.LocalDateTime
import java.time.ZoneOffset

data class Candidate(
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val phoneNumber: Int,
    val email: String,
    val birthday: LocalDateTime,
    val salaryClaim: Double,
    val notes: String,
    val image: URI
) {
    // Convert this Candidate to a CandidateDto
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDto(): CandidateDto {
        return CandidateDto(
            id = id ?: 0L, // Default to 0 if `id` is null for auto-generation
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            email = email,
            birthday = birthday.toEpochSecond(ZoneOffset.UTC),
            salaryClaim = salaryClaim,
            notes = notes,
            image = image
        )
    }

    companion object {
        // Convert a CandidateDto to a Candidate
        @RequiresApi(Build.VERSION_CODES.O)
        fun fromDto(candidateDto: CandidateDto): Candidate {
            return Candidate(
                id = if (candidateDto.id == 0L) null else candidateDto.id, // If `id` is 0, it's a new entry
                firstName = candidateDto.firstName,
                lastName = candidateDto.lastName,
                phoneNumber = candidateDto.phoneNumber,
                email = candidateDto.email,
                birthday = LocalDateTime.ofEpochSecond(candidateDto.birthday, 0, ZoneOffset.UTC),
                salaryClaim = candidateDto.salaryClaim,
                notes = candidateDto.notes,
                image = candidateDto.image
            )
        }
    }
}
