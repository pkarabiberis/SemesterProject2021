package com.example.kausiprojektikevat.domain.model.forecast

data class Forecast(
    val hourlyWeather: List<HourlyWeatherWithTimeStamp>,
    val dailyWeather: List<DailyWeather>
)

data class HourlyWeatherWithTimeStamp(
    val temp: Double,
    val ts: Long,
    val timeZone: Long,
    val icon: String
)

data class DailyWeather(
    val tempMin: Double,
    val tempMax: Double,
    val ts: Long,
    val timeZone: Long,
    val icon: String
)