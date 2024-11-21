package com.openclassrooms.vitesse.data.persistence.database

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.openclassrooms.vitesse.data.persistence.dao.CandidateDao
import com.openclassrooms.vitesse.data.persistence.dao.FavoriteDao
import com.openclassrooms.vitesse.data.persistence.entity.CandidateDto
import com.openclassrooms.vitesse.data.persistence.entity.FavoriteDto
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDate

@Database(entities = [CandidateDto::class, FavoriteDto::class], version = 1, exportSchema = true)
abstract class VitesseDatabase : RoomDatabase() {
    abstract fun candidateDao(): CandidateDao
    abstract fun favoriteDao(): FavoriteDao

    private class AppDatabaseCallback(
        private val scope: CoroutineScope,
        private val context: Context
    ) : Callback() {


        @RequiresApi(Build.VERSION_CODES.O)
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(context, database.candidateDao(), database.favoriteDao())
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
                    //.addCallback(AppDatabaseCallback(coroutineScope, context))

                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Prepopulate the database
         *
         * @param context The application context
         * @param candidateDao The candidate DAO
         * @param favoriteDao The favorite DAO
         */
        suspend fun populateDatabase(
            context: Context,
            candidateDao: CandidateDao,
            favoriteDao: FavoriteDao
        ) {
            withContext(Dispatchers.IO) {
                launch {
                    candidateDao.deleteAllCandidates()
                }
            }

            // Télécharge et enregistre les images, puis récupère leurs URI locaux
            val johnImageUri = downloadAndSaveImage(
                context,
                "https://xsgames.co/randomusers/assets/avatars/male/71.jpg",
                "john_image.jpg"
            )

            val martynaImageUri = downloadAndSaveImage(
                context,
                "https://xsgames.co/randomusers/assets/avatars/female/31.jpg",
                "michel_image.jpg"
            )

            // Ajouter les candidats avec leurs URI locaux
            candidateDao.addCandidate(
                CandidateDto(
                    id = 1,
                    firstName = "Ranjit",
                    lastName = "Singh",
                    phoneNumber = "0254546465",
                    email = "zfdfz@exemple.com",
                    birthday = getEpochDayFromLocalDate(1980, 1, 1),
                    salaryClaim = 1000.0,
                    notes = """
            Ranjit Singh has over 20 years of experience in logistics and supply chain management. 
            Known for his excellent organizational skills and ability to handle high-pressure environments. 
            Proficient in SAP and other enterprise resource planning (ERP) software. 
            Looking to leverage his expertise in a senior managerial role.
        """.trimIndent(),
                    image = johnImageUri?.toString() ?: ""
                )
            )

            candidateDao.addCandidate(
                CandidateDto(
                    id = 2,
                    firstName = "Martyna",
                    lastName = "Siddeswara",
                    phoneNumber = "0454685874",
                    email = "martyna@exemple.com",
                    birthday = getEpochDayFromLocalDate(1990, 1, 1),
                    salaryClaim = 1000.0,
                    notes = """
            Martyna Siddeswara is a dedicated software developer with 5 years of experience in Android development. 
            She is proficient in Kotlin, Java, and Jetpack Compose. 
            Martyna is known for her problem-solving skills and her ability to work effectively in agile teams. 
            She is passionate about building user-friendly applications and improving user experience.
        """.trimIndent(),
                    image = martynaImageUri?.toString() ?: ""
                )
            )

            // Utilisez `downloadAndSaveImage` pour télécharger les images et obtenir leurs URI locaux
            val candidate1ImageUri = downloadAndSaveImage(
                context,
                "https://xsgames.co/randomusers/assets/avatars/male/45.jpg",
                "candidate1_image.jpg"
            )
            val candidate2ImageUri = downloadAndSaveImage(
                context,
                "https://xsgames.co/randomusers/assets/avatars/female/15.jpg",
                "candidate2_image.jpg"
            )
            val candidate3ImageUri = downloadAndSaveImage(
                context,
                "https://xsgames.co/randomusers/assets/avatars/male/60.jpg",
                "candidate3_image.jpg"
            )
            val candidate4ImageUri = downloadAndSaveImage(
                context,
                "https://xsgames.co/randomusers/assets/avatars/female/20.jpg",
                "candidate4_image.jpg"
            )
            val candidate5ImageUri = downloadAndSaveImage(
                context,
                "https://xsgames.co/randomusers/assets/avatars/male/33.jpg",
                "candidate5_image.jpg"
            )

            val candidates = listOf(
                CandidateDto(
                    id = 3,
                    firstName = "Liam",
                    lastName = "O'Connor",
                    phoneNumber = "0457869831",
                    email = "liam.oconnor@example.com",
                    birthday = getEpochDayFromLocalDate(1985, 6, 15),
                    salaryClaim = 1200.0,
                    notes = "Candidate has extensive experience in project management.",
                    image = candidate1ImageUri?.toString() ?: ""
                ),
                CandidateDto(
                    id = 4,
                    firstName = "Sofia",
                    lastName = "Garcia",
                    phoneNumber = "0471254786",
                    email = "sofia.garcia@example.com",
                    birthday = getEpochDayFromLocalDate(1992, 3, 22),
                    salaryClaim = 1500.0,
                    notes = "Excellent team leader and problem solver.",
                    image = candidate2ImageUri?.toString() ?: ""
                ),
                CandidateDto(
                    id = 5,
                    firstName = "Ethan",
                    lastName = "Kowalski",
                    phoneNumber = "0462547982",
                    email = "ethan.kowalski@example.com",
                    birthday = getEpochDayFromLocalDate(1990, 11, 5),
                    salaryClaim = 1400.0,
                    notes = "Expert in software development and cloud solutions.",
                    image = candidate3ImageUri?.toString() ?: ""
                ),
                CandidateDto(
                    id = 6,
                    firstName = "Mia",
                    lastName = "Schneider",
                    phoneNumber = "0456893214",
                    email = "mia.schneider@example.com",
                    birthday = getEpochDayFromLocalDate(1987, 9, 12),
                    salaryClaim = 1300.0,
                    notes = "Specialist in marketing and brand management.",
                    image = candidate4ImageUri?.toString() ?: ""
                ),
                CandidateDto(
                    id = 7,
                    firstName = "Noah",
                    lastName = "Nguyen",
                    phoneNumber = "0461239784",
                    email = "noah.nguyen@example.com",
                    birthday = getEpochDayFromLocalDate(1995, 12, 8),
                    salaryClaim = 1100.0,
                    notes = "Proficient in data analysis and business intelligence.",
                    image = candidate5ImageUri?.toString() ?: ""
                )
            )
            candidates.forEach { candidateDao.addCandidate(it) }


            favoriteDao.addFavorite(FavoriteDto(1))
            favoriteDao.addFavorite(FavoriteDto(7))
            favoriteDao.addFavorite(FavoriteDto(3))
            favoriteDao.addFavorite(FavoriteDto(6))

        }

        private fun getEpochDayFromLocalDate(year: Int, month: Int, day: Int): Long {
            return LocalDate.of(year, month, day).toEpochDay()
        }


        private suspend fun downloadAndSaveImage(
            context: Context,
            imageUrl: String,
            fileName: String
        ): Uri? {
            return withContext(Dispatchers.IO) {
                try {
                    // Télécharger l'image en utilisant Picasso
                    val bitmap: Bitmap = Picasso.get().load(imageUrl).get()

                    // Définir le répertoire et le fichier de destination
                    val file = File(context.filesDir, fileName)

                    // Sauvegarder l'image dans le fichier
                    FileOutputStream(file).use { out ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    }

                    // Retourner l'URI local de l'image sauvegardée
                    Uri.fromFile(file)
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            }
        }


    }


}