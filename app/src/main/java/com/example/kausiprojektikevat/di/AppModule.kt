package com.example.kausiprojektikevat.di

import android.content.Context
import com.example.kausiprojektikevat.data.network.service.ApiService
import com.example.kausiprojektikevat.data.repository.LocationRepositoryImpl
import com.example.kausiprojektikevat.domain.repository.LocationRepository
import com.example.kausiprojektikevat.presentation.SemesterProjectApplication
import com.example.kausiprojektikevat.presentation.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLocationRepository(
        apiService: ApiService
    ): LocationRepository = LocationRepositoryImpl(apiService)
}