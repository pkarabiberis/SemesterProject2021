package com.example.kausiprojektikevat.domain.model.weather

data class WeatherDomain(
    val temp: Double,
    val feelsLike: Double,
    val windSpeed: Double,
    val humidity: Int
)