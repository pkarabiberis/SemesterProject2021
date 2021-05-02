package com.example.kausiprojektikevat.data.network.model.weather

import com.example.kausiprojektikevat.domain.model.weather.WeatherDomain
import com.google.gson.annotations.SerializedName

data class WeatherJson(

    @SerializedName("weather")
    var weather: List<Weather>,

    @SerializedName("main")
    var main: Main,

    @SerializedName("wind")
    var wind: Wind,

    @SerializedName("sys")
    var sys: Sys,

    @SerializedName("name")
    var name: String,

    @SerializedName("timezone")
    var timezone: Long
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val humidity: Int,
)

data class Sys(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Wind(
    val speed: Double,
    val deg: Double
)

fun WeatherJson.toDomainModel(): WeatherDomain =
    WeatherDomain(
        main.temp,
        main.feels_like,
        wind.speed,
        main.humidity
    )



