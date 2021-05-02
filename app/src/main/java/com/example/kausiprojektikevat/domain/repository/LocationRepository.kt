package com.example.kausiprojektikevat.domain.repository

import com.example.kausiprojektikevat.data.network.model.forecast.ForecastJson
import com.example.kausiprojektikevat.data.network.model.location.Location
import com.example.kausiprojektikevat.data.network.model.weather.WeatherJson
import com.example.kausiprojektikevat.domain.model.distance.Distance
import com.example.kausiprojektikevat.domain.model.forecast.Forecast
import com.example.kausiprojektikevat.domain.model.weather.WeatherDomain

interface LocationRepository {

    suspend fun getLocations(): List<Location>

    suspend fun getLocation(name: String): Location?

    suspend fun getDistanceFromCurrentLocation(
        origin: String?,
        destination: String?,
        apiKey: String
    ): Distance?

    suspend fun getDetailedWeather(
        lat: Double,
        lon: Double
    ): WeatherDomain

    suspend fun getForecast(
        lat: Double,
        lon: Double
    ): Forecast
}