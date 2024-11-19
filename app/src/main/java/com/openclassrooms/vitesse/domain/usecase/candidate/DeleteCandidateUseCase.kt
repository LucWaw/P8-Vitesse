package com.openclassrooms.vitesse.domain.usecase.candidate

import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import javax.inject.Inject

class DeleteCandidateUseCase @Inject constructor(private val candidateRepository: CandidateRepository) {
    suspend fun execute(candidate: Candidate) {
        candidateRepository.deleteCandidate(candidate)
    }
}