package com.example.kausiprojektikevat.domain.interactors

import com.example.kausiprojektikevat.domain.model.distance.Distance
import com.example.kausiprojektikevat.domain.repository.LocationRepository

class GetDistances(
    private val locationRepository: LocationRepository
) {

    suspend fun execute(
        origin: String?,
        destination: String?,
        apiKey: String
    ): Distance? = locationRepository.getDistanceFromCurrentLocation(
        origin,
        destination,
        apiKey
    )
}