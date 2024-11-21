package com.openclassrooms.vitesse.domain.usecase.favorite

import android.net.Uri
import com.openclassrooms.vitesse.data.persistence.repository.FavoriteRepository
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
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class GetAllFavoriteCandidateUseCaseTest {

    @Mock
    private lateinit var favoriteRepository: FavoriteRepository

    private lateinit var getAllFavoriteCandidateUseCase: GetAllFavoriteCandidateUseCase

    @BeforeEach
    fun setUp() {
        getAllFavoriteCandidateUseCase = GetAllFavoriteCandidateUseCase(favoriteRepository)
    }

    @Test
    fun `test when get all favorite candidates, it should return list of candidates`() = runTest {
        mockkStatic(Uri::class)
        every { Uri.parse(any()) } returns mockk()

        // Given
        val favoriteCandidates = listOf(
            Candidate(
                1, "John", "Doe", "0123456789", "john.doe@example.com",
                LocalDate.now(), 500.0, "Notes", Uri.parse("content://path/to/resource")
            ),
            Candidate(
                2, "Jane", "Doe", "0123456789", "jane.doe@example.com",
                LocalDate.now(), 600.0, "Notes", Uri.parse("content://path/to/resource")
            )
        )

        // Simulate that getAllFavoritesCandidates is called on the repository
        whenever(favoriteRepository.getAllFavoritesCandidates()).thenReturn(favoriteCandidates)

        // Act
        val result = getAllFavoriteCandidateUseCase.execute()

        // Assert
        assertEquals(2, result.size)
        assertEquals("John", result[0].firstName)
        assertEquals("Jane", result[1].firstName)
        verify(favoriteRepository, times(1)).getAllFavoritesCandidates()
    }
}