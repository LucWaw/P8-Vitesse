package com.openclassrooms.vitesse.domain.usecase.candidate

import android.net.Uri
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDate


@ExtendWith(MockitoExtension::class)
class AddCandidateUseCaseTest {

    @Mock
    private lateinit var candidateRepository: CandidateRepository

    @Mock
    private lateinit var addCandidateUseCase: AddCandidateUseCase

    @BeforeEach
    fun setUp() {
        addCandidateUseCase = AddCandidateUseCase(candidateRepository)
    }

    @Test
    fun `test when add candidate, candidate should be in return list`() = runTest {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()


        // Given
        val candidate = Candidate(
            0, "Doe", "John", "0123456789", "ddd@dd.fr",
            LocalDate.now(), 500.0, "Notes", Uri.parse("content://path/to/resource")

        )

        val fakeCandidates = mutableListOf(
            Candidate(
                1, "Doe", "Jane", "0123456789", "dzdzdz@dd.fr",
                LocalDate.now(), 500.0, "Notes", Uri.parse("content://path/to/resource")

            )
        )

        whenever ( candidateRepository.getAllCandidates() ).thenReturn(fakeCandidates)
        whenever ( candidateRepository.addCandidate(candidate) ).then{
            fakeCandidates.add(candidate)
        }

        //Act
        addCandidateUseCase.execute(candidate)
        val res = candidateRepository.getAllCandidates()

        //Assert
        assertEquals(fakeCandidates, res)


    }
}