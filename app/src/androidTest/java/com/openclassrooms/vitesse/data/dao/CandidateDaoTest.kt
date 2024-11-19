package com.openclassrooms.vitesse.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.vitesse.data.database.VitesseDatabase
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

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
}