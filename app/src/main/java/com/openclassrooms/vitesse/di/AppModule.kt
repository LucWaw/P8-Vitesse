package com.openclassrooms.vitesse.di

import android.content.Context
import com.openclassrooms.vitesse.data.dao.CandidateDao
import com.openclassrooms.vitesse.data.dao.FavoriteDao
import com.openclassrooms.vitesse.data.database.VitesseDatabase
import com.openclassrooms.vitesse.data.repository.CandidateRepository
import com.openclassrooms.vitesse.data.repository.FavoriteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context, coroutineScope: CoroutineScope): VitesseDatabase {
        return VitesseDatabase.getDatabase(context, coroutineScope)
    }

    @Provides
    fun provideCandidateDao(appDatabase: VitesseDatabase): CandidateDao {
        return appDatabase.candidateDao()
    }

    @Provides
    fun provideFavoriteDao(appDatabase: VitesseDatabase): FavoriteDao {
        return appDatabase.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideCandidateRepository(candidateDao: CandidateDao): CandidateRepository {
        return CandidateRepository(candidateDao)
    }

    @Provides
    @Singleton
    fun provideFavoriteRepository(favoriteDao: FavoriteDao): FavoriteRepository {
        return FavoriteRepository(favoriteDao)
    }
}