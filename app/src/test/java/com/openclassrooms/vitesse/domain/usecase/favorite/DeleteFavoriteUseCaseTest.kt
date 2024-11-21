package com.openclassrooms.vitesse.domain.usecase.favorite

import com.openclassrooms.vitesse.data.persistence.repository.FavoriteRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class DeleteFavoriteUseCaseTest {

    @Mock
    private lateinit var favoriteRepository: FavoriteRepository

    private lateinit var deleteFavoriteUseCase: DeleteFavoriteUseCase

    @BeforeEach
    fun setUp() {
        deleteFavoriteUseCase = DeleteFavoriteUseCase(favoriteRepository)
    }

    @Test
    fun `test when delete favorite, favorite should be deleted in repository`() = runTest {
        // Given
        val favoriteId = 1L

        // Simulate that deleteFavoriteById is called on the repository
        whenever(favoriteRepository.deleteFavoriteById(favoriteId)).thenReturn(Unit)

        // Act
        deleteFavoriteUseCase.execute(favoriteId)

        // Assert
        verify(favoriteRepository, times(1)).deleteFavoriteById(favoriteId)
    }
}
