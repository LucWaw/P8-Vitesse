package com.openclassrooms.vitesse.domain.usecase.candidate

import android.net.Uri
import com.openclassrooms.vitesse.data.persistence.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class DeleteCandidateUseCaseTest {

    @Mock
    private lateinit var candidateRepository: CandidateRepository

    private lateinit var deleteCandidateUseCase: DeleteCandidateUseCase

    @BeforeEach
    fun setUp() {
        deleteCandidateUseCase = DeleteCandidateUseCase(candidateRepository)
    }

    @Test
    fun `test when delete candidate, candidate should be deleted in repository`() = runTest {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()

        // Given
        val candidate = Candidate(
            1, "Doe", "John", "0123456789", "ddd@dd.fr",
            LocalDate.now(), 500.0, "Notes", Uri.parse("")
        )

        val fakeCandidates = mutableListOf(
            Candidate(
                1, "Doe", "Jane", "0123456789", "dzdzdz@dd.fr",
                LocalDate.now(), 500.0, "Notes", Uri.parse("content://path/to/resource")

            )
        )
        fakeCandidates.add(candidate)

        // Simulate that the candidate is deleted in the repository
        whenever(candidateRepository.deleteCandidate(candidate)).then {
            fakeCandidates.remove(candidate)
        }

        // Act
        deleteCandidateUseCase.execute(candidate)

        // Assert
        verify(candidateRepository, times(1)).deleteCandidate(candidate)
        assertFalse(fakeCandidates.contains(candidate))
    }
}
