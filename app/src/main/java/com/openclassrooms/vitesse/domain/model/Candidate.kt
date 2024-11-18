package com.openclassrooms.vitesse.domain.model

import android.net.Uri
import com.openclassrooms.vitesse.data.entity.CandidateDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

data class Candidate(
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val birthday: LocalDate,
    val salaryClaim: Double,
    val notes: String,
    val image: Uri
) {
    // Convert this Candidate to a CandidateDto
    fun toDto(): CandidateDto {
        return CandidateDto(
            id = id ?: 0L, // Default to 0 if `id` is null for auto-generation
            firstName = firstName,
            lastName = lastName,
            phoneNumber = phoneNumber,
            email = email,
            birthday = birthday.toEpochDay(),
            salaryClaim = salaryClaim,
            notes = notes,
            image = image.toString()
        )
    }

    companion object {
        // Convert a CandidateDto to a Candidate
        fun fromDto(candidateDto: CandidateDto): Candidate {
            return Candidate(
                id = if (candidateDto.id == 0L) null else candidateDto.id, // If `id` is 0, it's a new entry
                firstName = candidateDto.firstName,
                lastName = candidateDto.lastName,
                phoneNumber = candidateDto.phoneNumber,
                email = candidateDto.email,
                birthday = LocalDate.ofEpochDay(candidateDto.birthday),
                salaryClaim = candidateDto.salaryClaim,
                notes = candidateDto.notes,
                image = Uri.parse(candidateDto.image)
            )
        }
    }
}
