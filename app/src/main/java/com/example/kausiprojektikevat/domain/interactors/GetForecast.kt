package com.example.kausiprojektikevat.domain.interactors

import com.example.kausiprojektikevat.domain.model.forecast.Forecast
import com.example.kausiprojektikevat.domain.repository.LocationRepository

class GetForecast(
    private val locationRepository: LocationRepository
) {

    suspend fun execute(name: String): Forecast? {
        val location = locationRepository.getLocation(name)
        if (location != null) {
            return locationRepository.getForecast(
                location.lat!!,
                location.lon!!
            )
        }

        return null
    }
}