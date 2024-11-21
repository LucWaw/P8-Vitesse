package com.openclassrooms.vitesse.di

import android.content.Context
import com.openclassrooms.vitesse.data.api.network.CurrencyApi
import com.openclassrooms.vitesse.data.api.network.FallbackApi
import com.openclassrooms.vitesse.data.api.repository.CurrencyRepository
import com.openclassrooms.vitesse.data.persistence.dao.CandidateDao
import com.openclassrooms.vitesse.data.persistence.dao.FavoriteDao
import com.openclassrooms.vitesse.data.persistence.database.VitesseDatabase
import com.openclassrooms.vitesse.data.persistence.repository.CandidateRepository
import com.openclassrooms.vitesse.data.persistence.repository.FavoriteRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build()
    }

    @Provides
    @Singleton
    fun provideCurrencyApi(
        okHttpClient: OkHttpClient
    ): CurrencyApi {
        return Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/")
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
            .create(CurrencyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideFallbackApi(
        okHttpClient: OkHttpClient
    ): FallbackApi {
        return Retrofit.Builder()
            .baseUrl("https://latest.currency-api.pages.dev/")
            .client(okHttpClient)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            ).build()
            .create(FallbackApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCurrencyRepository(
        currencyApi: CurrencyApi,
        fallbackApi: FallbackApi
    ): CurrencyRepository {
        return CurrencyRepository(currencyApi, fallbackApi)
    }


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