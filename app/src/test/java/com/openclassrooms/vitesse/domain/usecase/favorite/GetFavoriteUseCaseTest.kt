package com.openclassrooms.vitesse.domain.usecase.favorite

import com.openclassrooms.vitesse.data.persistence.repository.FavoriteRepository
import com.openclassrooms.vitesse.domain.model.Favorite
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

@ExtendWith(MockitoExtension::class)
class GetFavoriteUseCaseTest {

    @Mock
    private lateinit var favoriteRepository: FavoriteRepository

    private lateinit var getFavoriteUseCase: GetFavoriteUseCase

    @BeforeEach
    fun setUp() {
        getFavoriteUseCase = GetFavoriteUseCase(favoriteRepository)
    }

    @Test
    fun `test when get favorite by id, it should return the correct favorite`() = runTest {
        // Given
        val favoriteId = 1L
        val favorite = Favorite(candidateId = favoriteId)

        // Simulate that the repository will return the correct favorite
        whenever(favoriteRepository.getFavoriteById(favoriteId)).thenReturn(favorite)

        // Act
        val result = getFavoriteUseCase.execute(favoriteId)

        // Assert
        assertNotNull(result)
        assertEquals(favoriteId, result?.candidateId)
        verify(favoriteRepository, times(1)).getFavoriteById(favoriteId)
    }

    @Test
    fun `test when favorite is not found, it should return null`() = runTest {
        // Given
        val favoriteId = 2L

        // Simulate that the repository will return null for the given favorite ID
        whenever(favoriteRepository.getFavoriteById(favoriteId)).thenReturn(null)

        // Act
        val result = getFavoriteUseCase.execute(favoriteId)

        // Assert
        assertNull(result)
        verify(favoriteRepository, times(1)).getFavoriteById(favoriteId)
    }
}
