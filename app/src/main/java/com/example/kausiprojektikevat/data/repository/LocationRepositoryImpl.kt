package com.example.kausiprojektikevat.data.repository

import com.example.kausiprojektikevat.EXCLUDE_PARAMETERS
import com.example.kausiprojektikevat.data.network.model.distance.toDomainModel
import com.example.kausiprojektikevat.data.network.model.forecast.ForecastJson
import com.example.kausiprojektikevat.data.network.model.forecast.toDomainModel
import com.example.kausiprojektikevat.data.network.model.location.Location
import com.example.kausiprojektikevat.data.network.model.weather.WeatherJson
import com.example.kausiprojektikevat.data.network.model.weather.toDomainModel
import com.example.kausiprojektikevat.data.network.service.ApiService
import com.example.kausiprojektikevat.domain.model.distance.Distance
import com.example.kausiprojektikevat.domain.model.forecast.Forecast
import com.example.kausiprojektikevat.domain.model.weather.WeatherDomain
import com.example.kausiprojektikevat.domain.repository.LocationRepository
import com.example.kausiprojektikevat.openWeatherMapAPIKey
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val api: ApiService
) : LocationRepository {

    override suspend fun getLocations(): List<Location> =
        FirebaseFirestore.getInstance().collection("locations").get().await()
            .toObjects(Location::class.java)

    override suspend fun getLocation(name: String): Location? =
        FirebaseFirestore.getInstance().collection("locations").document(
            name.capitalize(
                Locale.ROOT
            )
        ).get().await().toObject(Location::class.java)

    override suspend fun getDistanceFromCurrentLocation(
        origin: String?,
        destination: String?,
        apiKey: String
    ): Distance? {
        return api.getDistanceFromCurrentLocation(
            origin,
            destination,
            apiKey
        ).toDomainModel()
    }

    override suspend fun getDetailedWeather(
        lat: Double,
        lon: Double
    ): WeatherDomain {
        return api.getDetailedWeather(
            lat,
            lon,
            openWeatherMapAPIKey,
            "metric"
        ).toDomainModel()
    }

    override suspend fun getForecast(
        lat: Double, lon: Double
    ): Forecast {
        return api.getForecast(
            lat,
            lon,
            EXCLUDE_PARAMETERS,
            "metric",
            openWeatherMapAPIKey
        ).toDomainModel()
    }
}