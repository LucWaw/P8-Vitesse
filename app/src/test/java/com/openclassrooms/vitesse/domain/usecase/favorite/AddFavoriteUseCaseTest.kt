package com.openclassrooms.vitesse.domain.usecase.favorite

import com.openclassrooms.vitesse.data.persistence.repository.FavoriteRepository
import com.openclassrooms.vitesse.domain.model.Favorite
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
class AddFavoriteUseCaseTest {

    @Mock
    private lateinit var favoriteRepository: FavoriteRepository

    private lateinit var addFavoriteUseCase: AddFavoriteUseCase

    @BeforeEach
    fun setUp() {
        addFavoriteUseCase = AddFavoriteUseCase(favoriteRepository)
    }

    @Test
    fun `test when add favorite, favorite should be added in repository`() = runTest {
        // Given
        val favorite = Favorite(candidateId = 1L)

        // Simulate that addFavorite is called on the repository
        whenever(favoriteRepository.addFavorite(favorite)).thenReturn(Unit)

        // Act
        addFavoriteUseCase.execute(favorite)

        // Assert
        verify(favoriteRepository, times(1)).addFavorite(favorite)
    }
}
