package com.openclassrooms.vitesse.data.dao

import android.net.Uri
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.openclassrooms.vitesse.data.persistence.database.VitesseDatabase
import com.openclassrooms.vitesse.data.persistence.entity.CandidateDto
import com.openclassrooms.vitesse.data.persistence.entity.FavoriteDto
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class CandidateDaoTest {
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
    fun testShouldInsertCandidateIntoDatabaseSuccessfully() = runTest {
        // Given
        val candidate = CandidateDto(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "fef@f.fr",
            phoneNumber = "123456789",
            birthday = LocalDate.now().toEpochDay(),
            salaryClaim = 1000.0,
            notes = "notes",
            image = Uri.EMPTY.toString()
        )

        // When
        database.candidateDao().addCandidate(candidate)


        // Then
        val insertedCandidate = database.candidateDao().getCandidateById(1)
        assertEquals("Inserted and retrieved candidates must be equals", candidate, insertedCandidate)
    }

    @Test
    fun testShouldDeleteCandidateById() = runTest {
        // Given
        val candidate = CandidateDto(
            id = 1,
            firstName = "Jane",
            lastName = "Doe",
            email = "jane@test.com",
            phoneNumber = "987654321",
            birthday = LocalDate.now().toEpochDay(),
            salaryClaim = 1200.0,
            notes = "Delete test",
            image = Uri.EMPTY.toString()
        )
        database.candidateDao().addCandidate(candidate)

        // When
        database.candidateDao().deleteCandidateById(1)

        // Then
        val deletedCandidate = database.candidateDao().getCandidateById(1)
        assertNull("Candidate should be null after deletion", deletedCandidate)
    }

    @Test
    fun testShouldUpdateCandidateSuccessfully() = runTest {
        // Given
        val initialCandidate = CandidateDto(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@test.com",
            phoneNumber = "123456789",
            birthday = LocalDate.now().toEpochDay(),
            salaryClaim = 1000.0,
            notes = "Initial notes",
            image = Uri.EMPTY.toString()
        )

        database.candidateDao().addCandidate(initialCandidate)

        val updatedCandidate = CandidateDto(
            id = 1, // ID must be the same
            firstName = "Johnny",
            lastName = "Doe",
            email = "johnny.doe@test.com",
            phoneNumber = "987654321",
            birthday = LocalDate.now().toEpochDay(),
            salaryClaim = 1500.0,
            notes = "Updated notes",
            image = Uri.EMPTY.toString()
        )

        // When
        database.candidateDao().addCandidate(updatedCandidate)

        // Then
        val retrievedCandidate = database.candidateDao().getCandidateById(1)
        assertNotNull("Updated candidate should exist in the database", retrievedCandidate)
        assertEquals("Updated candidate should match the new values", updatedCandidate, retrievedCandidate)
    }

    @Test
    fun testGetAllCandidtaeShouldReturnEmptyList() = runTest {
        database.candidateDao().getAllCandidates().test {
            val candidates = awaitItem()
            assertEquals("Initial list of candidates should be empty", 0, candidates.size)
            cancel()
        }
    }

    @Test
    fun testGetAllCandidatesShouldReturnNonEmptyList() = runTest {
        // Given
        val candidates = listOf(
            CandidateDto(
                id = 0, // ID must be the same
                firstName = "Johnny",
                lastName = "Doe",
                email = "johnny.doe@test.com",
                phoneNumber = "987654321",
                birthday = LocalDate.now().toEpochDay(),
                salaryClaim = 1500.0,
                notes = "Updated notes",
                image = Uri.EMPTY.toString()
            ), CandidateDto(
                id = 0,
                firstName = "John",
                lastName = "Doe",
                email = "john.doe@test.com",
                phoneNumber = "123456789",
                birthday = LocalDate.now().toEpochDay(),
                salaryClaim = 1000.0,
                notes = "Initial notes",
                image = Uri.EMPTY.toString()
            )
        )

        candidates.forEach {
            database.candidateDao().addCandidate(it)
        }

        // When
        database.candidateDao().getAllCandidates().test {
            // Then
            val results = awaitItem()
            assertEquals(
                "Inserted and retrieved list must have the same size",
                candidates.size,
                results.size
            )
            cancel()
        }
    }

    @Test
    fun testDeleteCandidateShouldDeleteAssociatedFavorites() = runTest {
        // Given
        val candidate = CandidateDto(
            id = 0, // ID must be the same
            firstName = "Johnny",
            lastName = "Doe",
            email = "johnny.doe@test.com",
            phoneNumber = "987654321",
            birthday = LocalDate.now().toEpochDay(),
            salaryClaim = 1500.0,
            notes = "Updated notes",
            image = Uri.EMPTY.toString()
        )

        val candidateId  = database.candidateDao().addCandidate(candidate)

        val favorite = FavoriteDto(
            candidateId = candidateId
        )

        database.favoriteDao().addFavorite(favorite)

        // When
        database.candidateDao().deleteCandidateById(candidate.id)

        // Then
        val deletedFavorite = database.favoriteDao().getFavoriteById(candidate.id)
        assertNull("Favorite should be null after deletion", deletedFavorite)
    }



}