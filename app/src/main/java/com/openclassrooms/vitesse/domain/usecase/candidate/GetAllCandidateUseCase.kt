package com.openclassrooms.vitesse.domain.usecase.candidate

import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import javax.inject.Inject

class GetAllCandidateUseCase @Inject constructor(private val candidateRepository: CandidateRepository) {
    suspend fun execute() : List<Candidate> {
        return candidateRepository.getAllCandidates()
    }
}