package com.example.kausiprojektikevat.di

import com.example.kausiprojektikevat.domain.interactors.GetDistances
import com.example.kausiprojektikevat.domain.interactors.GetForecast
import com.example.kausiprojektikevat.domain.interactors.GetLocationWeather
import com.example.kausiprojektikevat.domain.interactors.GetLocations
import com.example.kausiprojektikevat.domain.repository.LocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideGetDistances(
        locationRepository: LocationRepository
    ): GetDistances = GetDistances(locationRepository)

    @ViewModelScoped
    @Provides
    fun provideGetLocations(
        locationRepository: LocationRepository
    ): GetLocations = GetLocations(locationRepository)

    @ViewModelScoped
    @Provides
    fun provideGetLocationWeather(
        locationRepository: LocationRepository
    ): GetLocationWeather = GetLocationWeather(locationRepository)

    @ViewModelScoped
    @Provides
    fun provideGetForecast(
        locationRepository: LocationRepository
    ): GetForecast = GetForecast(locationRepository)
}