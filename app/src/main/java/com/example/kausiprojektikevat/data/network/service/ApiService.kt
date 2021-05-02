package com.example.kausiprojektikevat.data.network.service


import com.example.kausiprojektikevat.data.network.model.distance.DistanceJson
import com.example.kausiprojektikevat.data.network.model.forecast.ForecastJson
import com.example.kausiprojektikevat.data.network.model.weather.WeatherJson
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("https://api.openweathermap.org/data/2.5/weather")
    suspend fun getDetailedWeather(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): WeatherJson

    @GET("https://api.openweathermap.org/data/2.5/onecall")
    suspend fun getForecast(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("exclude") exclude: String?,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ): ForecastJson

    @GET("https://maps.googleapis.com/maps/api/directions/json")
    suspend fun getDistanceFromCurrentLocation(
        @Query("origin") latLngOrigin: String?,
        @Query("destination") latLngDestination: String?,
        @Query("key") apiKey: String
    ): DistanceJson
}