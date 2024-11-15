package com.openclassrooms.vitesse.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.dao.FavoriteDao
import com.openclassrooms.vitesse.data.entity.CandidateDto
import com.openclassrooms.vitesse.data.entity.FavoriteDto

@Database(entities = [CandidateDto::class, FavoriteDto::class], version = 1, exportSchema = true)
abstract class VitesseDatabase : RoomDatabase(){
    abstract fun candidateDao(): CandidateDao
    abstract fun favoriteDao(): FavoriteDao



    companion object {
        @Volatile
        private var INSTANCE: VitesseDatabase? = null


        fun getDatabase(context: Context): VitesseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VitesseDatabase::class.java,
                    "AristaDB"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }


    }
}