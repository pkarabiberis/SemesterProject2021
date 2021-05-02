package com.example.kausiprojektikevat.data.network.model.forecast

import com.example.kausiprojektikevat.domain.model.forecast.DailyWeather
import com.example.kausiprojektikevat.domain.model.forecast.Forecast
import com.example.kausiprojektikevat.domain.model.forecast.HourlyWeatherWithTimeStamp

data class ForecastJson(
    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Long
) {
    data class Current(
        val clouds: Int,
        val dew_point: Double,
        val dt: Int,
        val feels_like: Double,
        val humidity: Int,
        val pressure: Int,
        val snow: Snow,
        val sunrise: Int,
        val sunset: Int,
        val temp: Double,
        val uvi: Double,
        val visibility: Int,
        val weather: List<Weather>,
        val wind_deg: Int,
        val wind_gust: Double,
        val wind_speed: Double
    ) {
        data class Snow(
            val `1h`: Double
        )

        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        )
    }

    data class Daily(
        val clouds: Int,
        val dew_point: Double,
        val dt: Long,
        val feels_like: FeelsLike,
        val humidity: Int,
        val pop: Float,
        val pressure: Int,
        val snow: Double,
        val sunrise: Int,
        val sunset: Int,
        val temp: Temp,
        val uvi: Double,
        val weather: List<Weather>,
        val wind_deg: Int,
        val wind_speed: Double
    ) {
        data class FeelsLike(
            val day: Double,
            val eve: Double,
            val morn: Double,
            val night: Double
        )

        data class Temp(
            val day: Double,
            val eve: Double,
            val max: Double,
            val min: Double,
            val morn: Double,
            val night: Double
        )

        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        )
    }

    data class Hourly(
        val clouds: Int,
        val dew_point: Double,
        val dt: Long,
        val feels_like: Double,
        val humidity: Int,
        val pop: Double,
        val pressure: Int,
        val snow: Snow,
        val temp: Double,
        val uvi: Double,
        val visibility: Int,
        val weather: List<Weather>,
        val wind_deg: Int,
        val wind_gust: Double,
        val wind_speed: Double
    ) {
        data class Snow(
            val `1h`: Double
        )

        data class Weather(
            val description: String,
            val icon: String,
            val id: Int,
            val main: String
        )
    }
}

fun ForecastJson.toDomainModel(): Forecast {
    val hourlyTemps = mutableListOf<HourlyWeatherWithTimeStamp>()
    val dailyTemps = mutableListOf<DailyWeather>()

    hourly.forEach {
        hourlyTemps.add(HourlyWeatherWithTimeStamp(it.temp, it.dt, timezone_offset, it.weather[0].icon))
    }

    daily.forEach {
        dailyTemps.add(DailyWeather(it.temp.min, it.temp.max, it.dt, timezone_offset, it.weather[0].icon))
    }

    return Forecast(hourlyTemps, dailyTemps)
}
