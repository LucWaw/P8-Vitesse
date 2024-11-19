package com.openclassrooms.vitesse.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.openclassrooms.vitesse.data.database.VitesseDatabase
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.FavoriteDto
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteDaoTest {

    private lateinit var database: VitesseDatabase

    @Before
    fun createDb() {
        database = Room
            .inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), VitesseDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testShouldInsertFavoriteSuccessfully() = runTest {
        // Given
        val candidate = CandidateDto(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@test.com",
            phoneNumber = "123456789",
            birthday = System.currentTimeMillis(),
            salaryClaim = 1000.0,
            notes = "Notes",
            image = ""
        )
        database.candidateDao().addCandidate(candidate)

        val favorite = FavoriteDto(candidateId = 1)

        // When
        database.favoriteDao().addFavorite(favorite)

        // Then
        val retrievedFavorite = database.favoriteDao().getFavoriteById(1)
        assertNotNull("Favorite should be successfully inserted", retrievedFavorite)
        assertEquals("Favorite should match the inserted data", favorite, retrievedFavorite)
    }

    @Test
    fun testShouldRetrieveFavoriteIds() = runTest {
        // Given
        val candidate = CandidateDto(
            id = 0,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@test.com",
            phoneNumber = "123456789",
            birthday = System.currentTimeMillis(),
            salaryClaim = 1000.0,
            notes = "Notes",
            image = ""
        )

        database.candidateDao().addCandidate(candidate)
        database.candidateDao().addCandidate(candidate)
        database.candidateDao().addCandidate(candidate)

        val favoriteIds = listOf(1L, 2L, 3L)
        favoriteIds.forEach {
            val favorite = FavoriteDto(candidateId = it)
            database.favoriteDao().addFavorite(favorite)
        }

        // When
        database.favoriteDao().getFavoriteIds().test {
            val retrievedIds = awaitItem()

            // Then
            assertEquals("The retrieved IDs should match the inserted IDs", favoriteIds, retrievedIds)
            cancel()
        }
    }

    @Test
    fun testShouldRetrieveFavoriteById() = runTest {
        // Given
        val candidate = CandidateDto(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@test.com",
            phoneNumber = "123456789",
            birthday = System.currentTimeMillis(),
            salaryClaim = 1000.0,
            notes = "Notes",
            image = ""
        )
        database.candidateDao().addCandidate(candidate)

        val favorite = FavoriteDto(candidateId = 1)
        database.favoriteDao().addFavorite(favorite)

        // When
        val retrievedFavorite = database.favoriteDao().getFavoriteById(1)

        // Then
        assertNotNull("Favorite should exist in the database", retrievedFavorite)
        assertEquals("Retrieved favorite should match the inserted favorite", favorite, retrievedFavorite)
    }

    @Test
    fun testShouldRetrieveCandidatesByFavoriteIds() = runTest {
        // Given
        val candidates = listOf(
            CandidateDto(
                id = 1,
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@test.com",
                phoneNumber = "123456789",
                birthday = System.currentTimeMillis(),
                salaryClaim = 1000.0,
                notes = "Notes",
                image = ""
            ),
            CandidateDto(
                id = 2,
                firstName = "Jane",
                lastName = "Smith",
                email = "jane.smith@test.com",
                phoneNumber = "987654321",
                birthday = System.currentTimeMillis(),
                salaryClaim = 1200.0,
                notes = "Notes",
                image = ""
            )
        )
        candidates.forEach {
            database.candidateDao().addCandidate(it)
        }

        val favorites = candidates.map { FavoriteDto(candidateId = it.id) }
        favorites.forEach {
            database.favoriteDao().addFavorite(it)
        }

        // When
        database.favoriteDao().getCandidatesByIds(favorites.map { it.candidateId }).test {
            val retrievedCandidates = awaitItem()

            // Then
            assertEquals(
                "The retrieved candidates should match the candidates associated with the favorite IDs",
                candidates.size,
                retrievedCandidates.size
            )
            cancel()
        }
    }

    @Test
    fun testShouldDeleteFavoriteById() = runTest {
        // Given
        val candidate = CandidateDto(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@test.com",
            phoneNumber = "123456789",
            birthday = System.currentTimeMillis(),
            salaryClaim = 1000.0,
            notes = "Notes",
            image = ""
        )
        database.candidateDao().addCandidate(candidate)

        val favorite = FavoriteDto(candidateId = 1)
        database.favoriteDao().addFavorite(favorite)

        // When
        database.favoriteDao().deleteFavoriteById(1)

        // Then
        val deletedFavorite = database.favoriteDao().getFavoriteById(1)
        assertNull("Favorite should be null after deletion", deletedFavorite)
    }
}