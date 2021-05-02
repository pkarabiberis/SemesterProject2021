package com.example.kausiprojektikevat.domain.interactors

import com.example.kausiprojektikevat.data.network.model.weather.WeatherJson
import com.example.kausiprojektikevat.domain.model.weather.WeatherDomain
import com.example.kausiprojektikevat.domain.repository.LocationRepository

class GetLocationWeather(
    private val locationRepository: LocationRepository
) {

    suspend fun execute(name: String): WeatherDomain? {
        val location = locationRepository.getLocation(name)
        if (location != null) {
            return locationRepository.getDetailedWeather(location.lat!!, location.lon!!)
        }

        return null
    }
}