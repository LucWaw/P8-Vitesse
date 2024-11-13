package com.openclassrooms.vitesse.data.database

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.dao.FavoriteDao
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.FavoriteDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset

@Database(entities = [CandidateDto::class, FavoriteDto::class], version = 1, exportSchema = true)
abstract class VitesseDatabase : RoomDatabase(){
    abstract fun candidateDao(): CandidateDao
    abstract fun favoriteDao(): FavoriteDao


    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {


        @RequiresApi(Build.VERSION_CODES.O)
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.candidateDao(), database.favoriteDao())
                }
            }
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: VitesseDatabase? = null


        fun getDatabase(context: Context, coroutineScope: CoroutineScope): VitesseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitesseDatabase::class.java,
                    "AristaDB"
                )
                    .addCallback(AppDatabaseCallback(coroutineScope))
                    .build()
                INSTANCE = instance
                instance
            }
        }


        /**
         * Prepopulate the database
         *
         * @param candidateDao the candidate DAO
         * @param favoriteDao the favorite DAO
         */
        @RequiresApi(Build.VERSION_CODES.O)
        suspend fun populateDatabase(candidateDao: CandidateDao, favoriteDao: FavoriteDao) {
            withContext(Dispatchers.IO) {
                launch {
                    candidateDao.deleteAll()
                }
            }


            candidateDao.addCandidate(
                CandidateDto(
                    id = 1,
                    firstName = "John",
                    lastName = "Doe",
                    phoneNumber = "0254546465",
                    email = "zfdfz@exemple.com",
                    birthday = LocalDateTime.now().minusDays(1).atZone(ZoneOffset.UTC).toInstant()
                        .toEpochMilli(),
                    salaryClaim = 1000.0,
                    notes = "Notes",
                    image = Uri.parse("https://www.example.com").toString()
                )
            )

            candidateDao.addCandidate(
                CandidateDto(
                    id = 2,
                    firstName = "Michel",
                    lastName = "Truc",
                    phoneNumber = "0254546465",
                    email = "zfdfz@exemple.com",
                    birthday = LocalDateTime.now().minusDays(1).atZone(ZoneOffset.UTC).toInstant()
                        .toEpochMilli(),
                    salaryClaim = 1000.0,
                    notes = "NotesEEEE",
                    image = Uri.parse("https://www.example.com").toString()
                )
            )

            favoriteDao.addFavorite(FavoriteDto(1))



        }
    }
}