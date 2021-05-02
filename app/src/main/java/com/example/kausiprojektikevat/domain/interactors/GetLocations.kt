package com.example.kausiprojektikevat.domain.interactors

import com.example.kausiprojektikevat.domain.repository.LocationRepository

class GetLocations(
    private val locationRepository: LocationRepository
) {

    suspend fun execute() = locationRepository.getLocations()
}