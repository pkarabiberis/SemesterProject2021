package com.example.kausiprojektikevat.presentation.ui.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kausiprojektikevat.TAG
import com.example.kausiprojektikevat.data.network.model.location.Location
import com.example.kausiprojektikevat.domain.interactors.GetForecast
import com.example.kausiprojektikevat.domain.interactors.GetLocationWeather
import com.example.kausiprojektikevat.domain.model.forecast.Forecast
import com.example.kausiprojektikevat.presentation.ui.convertToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getLocationWeather: GetLocationWeather,
    private val getForecast: GetForecast
) : ViewModel() {

    private val _temperature = MutableLiveData<Double>()
    val temperature: LiveData<Double>
        get() = _temperature

    private val _temperatureFeelsLike = MutableLiveData<Double>()
    val temperatureFeelsLike: LiveData<Double>
        get() = _temperatureFeelsLike

    private val _humidity = MutableLiveData<Int>()
    val humidity: LiveData<Int>
        get() = _humidity

    private val _windSpeed = MutableLiveData<Double>()
    val windSpeed: LiveData<Double>
        get() = _windSpeed

    private val _forecast = MutableLiveData<Forecast>()
    val forecast: LiveData<Forecast>
        get() = _forecast
    
    fun getLocation(name: String) = viewModelScope.launch {
        val weather = getLocationWeather.execute(name)
        weather?.let {
            with(it) {
                _temperature.postValue(temp)
                _temperatureFeelsLike.postValue(feelsLike)
                _humidity.postValue(humidity)
                _windSpeed.postValue(windSpeed)
            }
        }
    }

    fun getForecast(name: String) = viewModelScope.launch {
        _forecast.postValue(getForecast.execute(name))
    }
}