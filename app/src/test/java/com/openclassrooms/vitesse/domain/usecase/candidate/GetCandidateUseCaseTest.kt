package com.openclassrooms.vitesse.domain.usecase.candidate

import android.net.Uri
import com.openclassrooms.vitesse.data.persistence.repository.CandidateRepository
import com.openclassrooms.vitesse.domain.model.Candidate
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class GetCandidateUseCaseTest {

    @Mock
    private lateinit var candidateRepository: CandidateRepository

    private lateinit var getCandidateUseCase: GetCandidateUseCase

    @BeforeEach
    fun setUp() {
        getCandidateUseCase = GetCandidateUseCase(candidateRepository)
    }

    @Test
    fun `test when get candidate by id, should return candidate`() = runTest {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()


        // Given
        val candidateId = 1L
        val expectedCandidate = Candidate(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "123456789",
            email = "john.doe@example.com",
            birthday = LocalDate.now(),
            salaryClaim = 50000.0,
            notes = "Test notes",
            image = Uri.parse("")
        )

        // Simulate repository returning a candidate
        whenever(candidateRepository.getCandidateById(candidateId)).thenReturn(expectedCandidate)

        // Act
        val result = getCandidateUseCase.execute(candidateId)

        // Assert
        assertNotNull(result)
        assertEquals(expectedCandidate, result)
    }

    @Test
    fun `test when candidate not found, should return null`() = runTest {
        // Given
        val candidateId = 999L

        // Simulate repository returning null (candidate not found)
        whenever(candidateRepository.getCandidateById(candidateId)).thenReturn(null)

        // Act
        val result = getCandidateUseCase.execute(candidateId)

        // Assert
        assertNull(result)
    }
}
