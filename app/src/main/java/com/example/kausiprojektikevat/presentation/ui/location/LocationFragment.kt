package com.example.kausiprojektikevat.presentation.ui.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kausiprojektikevat.R
import com.example.kausiprojektikevat.TAG
import com.example.kausiprojektikevat.databinding.FragmentLocationBinding
import com.example.kausiprojektikevat.domain.model.forecast.DailyWeather
import com.example.kausiprojektikevat.domain.model.forecast.HourlyWeatherWithTimeStamp
import com.example.kausiprojektikevat.presentation.ui.location.recyclerview.ForecastAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class LocationFragment : Fragment(R.layout.fragment_location) {

    private lateinit var binding: FragmentLocationBinding
    private val viewModel: LocationViewModel by viewModels()
    private lateinit var locationName: String
    private lateinit var picUrl: String
    private lateinit var forecastAdapter: ForecastAdapter
    private var hourlyWeathers = listOf<HourlyWeatherWithTimeStamp>()
    private var dailyWeathers = listOf<DailyWeather>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with (arguments) {
            this?.getString("locationName")?.let { name ->
                locationName = name
                (activity as AppCompatActivity).supportActionBar?.title = locationName.capitalize(Locale.ROOT)
            }

            this?.getString("picUrl")?.let { url ->
                picUrl = url
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLocationBinding.bind(view)
        viewModel.getLocation(locationName)
        viewModel.getForecast(locationName)
        setupRecyclerView()
        subscribeToObservers()

        binding.dailyBtn.setOnClickListener {
            forecastAdapter.apply {
                clearHourlyForecast()
                submitDailyForecast(dailyWeathers)
                binding.hourlyBtn.alpha = .44f
                it.alpha = 1f
            }
        }

        binding.hourlyBtn.setOnClickListener {
            forecastAdapter.apply {
                clearDailyForecast()
                submitItems(hourlyWeathers)
                binding.dailyBtn.alpha = .44f
                it.alpha = 1f
            }
        }
    }

    private fun setupRecyclerView() = binding.rvForecasts.apply {
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        forecastAdapter = ForecastAdapter()
        adapter = forecastAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun subscribeToObservers() {
        viewModel.temperature.observe(viewLifecycleOwner, {
            binding.locationTemperatureTextView.text = if (it.toString().startsWith("-")) {
                "${it.toString().take(2)}°"
            } else {
                "${it.toString().first()}°"
            }

            Log.d(TAG, "subscribeToObservers: ${"${it.toString()} °C"}")
        })

        viewModel.temperatureFeelsLike.observe(viewLifecycleOwner, {
            binding.feelsLikeTextView.text = if (it.toString().startsWith("-")) {
                "${it.toString().take(2)}°"
            } else {
                "${it.toString().first()}°"
            }
        })

        viewModel.humidity.observe(viewLifecycleOwner, {
            binding.humidityTextView.text = "${it.toString()}%"
        })

        viewModel.windSpeed.observe(viewLifecycleOwner, {
            binding.windSpeedTextView.text = "${it.toString()} m/s"
        })

        viewModel.forecast.observe(viewLifecycleOwner, {
            forecastAdapter.submitItems(it.hourlyWeather)
            hourlyWeathers = it.hourlyWeather
            dailyWeathers = it.dailyWeather
        })
    }
}