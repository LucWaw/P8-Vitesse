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
class GetAllCandidateUseCaseTest {

    @Mock
    private lateinit var candidateRepository: CandidateRepository

    private lateinit var getAllCandidateUseCase: GetAllCandidateUseCase

    @BeforeEach
    fun setUp() {
        getAllCandidateUseCase = GetAllCandidateUseCase(candidateRepository)
    }

    @Test
    fun `test when get all candidates, should return list of candidates`() = runTest {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()

        // Given
        val expectedCandidates = listOf(
            Candidate(
                id = 1,
                firstName = "John",
                lastName = "Doe",
                phoneNumber = "123456789",
                email = "john.doe@example.com",
                birthday = LocalDate.now(),
                salaryClaim = 50000.0,
                notes = "Test notes",
                image = Uri.parse("")
            ),
            Candidate(
                id = 2,
                firstName = "Jane",
                lastName = "Smith",
                phoneNumber = "987654321",
                email = "jane.smith@example.com",
                birthday = LocalDate.now(),
                salaryClaim = 55000.0,
                notes = "Test notes 2",
                image = Uri.parse("")
            )
        )

        // Simulate repository returning a list of candidates
        whenever(candidateRepository.getAllCandidates()).thenReturn(expectedCandidates)

        // Act
        val result = getAllCandidateUseCase.execute()

        // Assert
        assertEquals(expectedCandidates, result)
    }

    @Test
    fun `test when get all candidates, should return empty list`() = runTest {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()

        // Given
        val expectedCandidates = emptyList<Candidate>()

        // Simulate repository returning an empty list
        whenever(candidateRepository.getAllCandidates()).thenReturn(expectedCandidates)

        // Act
        val result = getAllCandidateUseCase.execute()

        // Assert
        assertTrue(result.isEmpty())
    }

}
