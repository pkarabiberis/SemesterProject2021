package com.example.kausiprojektikevat.presentation.ui.location.recyclerview

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kausiprojektikevat.R
import com.example.kausiprojektikevat.TAG
import com.example.kausiprojektikevat.domain.model.forecast.DailyWeather
import com.example.kausiprojektikevat.domain.model.forecast.HourlyWeatherWithTimeStamp
import com.example.kausiprojektikevat.presentation.ui.convertToDate
import kotlinx.android.synthetic.main.list_item_forecast.view.*

class ForecastAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val temperatures = mutableListOf<HourlyWeatherWithTimeStamp>()
    private val dailyForecast = mutableListOf<DailyWeather>()

    fun submitItems(temperatures: List<HourlyWeatherWithTimeStamp>) {
        if (this.temperatures.isEmpty()) {
            this.temperatures.addAll(temperatures)
            notifyDataSetChanged()
        }
    }

    fun submitDailyForecast(dailyForecast: List<DailyWeather>) {
        if (this.dailyForecast.isEmpty()) {
            this.dailyForecast.addAll(dailyForecast)
            notifyDataSetChanged()
        }
    }

    fun clearHourlyForecast() {
        temperatures.clear()
    }

    fun clearDailyForecast() {
        dailyForecast.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_forecast,
            parent,
            false
        )
        return ForecastViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ForecastViewHolder -> {
                if (temperatures.isNotEmpty()) {
                    holder.bind(temperatures[position], null)
                } else {
                    holder.bind(null, dailyForecast[position])
                }
            }
        }
    }

    override fun getItemCount(): Int = temperatures.size + dailyForecast.size

    inner class ForecastViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(
            forecast: HourlyWeatherWithTimeStamp?,
            dailyForecast: DailyWeather?
        ) {
            Log.d(TAG, "bind: ")

            if (forecast != null) {
                val iconUrl = "${forecast?.icon}@2x.png"

                itemView.forecastTextView.text = if (forecast?.temp.toString().startsWith("-")) {
                    "${forecast?.temp.toString().take(2)}°"
                } else {
                    "${forecast?.temp.toString().first()}°"
                }
                val dayOfWeek = when (forecast?.ts?.let { convertToDate(it).substringBefore(",") }) {
                    "Monday" -> "Maanantai"
                    "Tuesday" -> "Tiistai"
                    "Wednesday" -> "Keskiviikko"
                    "Thursday" -> "Torstai"
                    "Friday" -> "Perjantai"
                    "Saturday" -> "Lauantai"
                    "Sunday" -> "Sunnuntai"
                    else -> ""
                }

                itemView.apply {
                    dailyMinMaxTemp.visibility = View.INVISIBLE
                    distanceTextView.visibility = View.VISIBLE
                    forecastTextView.visibility = View.VISIBLE
                    weekOfDayTextView.text = dayOfWeek
                    distanceTextView.text = forecast?.ts?.let { convertToDate(it).substringAfterLast(" ") }
                }

                Glide
                    .with(itemView.context)
                    .load("https://openweathermap.org/img/wn/${iconUrl}")
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean = false

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            itemView.foreCastImageLoader.visibility = View.GONE
                            return false
                        }
                    })
                    .into(itemView.locationImageView)
            } else {
                val iconUrl = "${dailyForecast?.icon}@2x.png"

                val tempMax = if (dailyForecast?.tempMax.toString().startsWith("-")) {
                    "${dailyForecast?.tempMax.toString().take(2)}°"
                } else {
                    "${dailyForecast?.tempMax.toString().first()}°"
                }

                val tempMin = if (dailyForecast?.tempMin.toString().startsWith("-")) {
                    "${dailyForecast?.tempMin.toString().take(2)}°"
                } else {
                    "${dailyForecast?.tempMin.toString().first()}°"
                }


                val dayOfWeek = when (dailyForecast?.ts?.let { convertToDate(it).substringBefore(",") }) {
                    "Monday" -> "Maanantai"
                    "Tuesday" -> "Tiistai"
                    "Wednesday" -> "Keskiviikko"
                    "Thursday" -> "Torstai"
                    "Friday" -> "Perjantai"
                    "Saturday" -> "Lauantai"
                    "Sunday" -> "Sunnuntai"
                    else -> ""
                }

                itemView.apply {
                    weekOfDayTextView.text = dayOfWeek
                    dailyMinMaxTemp.text = "${tempMax}/${tempMin}"
                    dailyMinMaxTemp.visibility = View.VISIBLE
                    distanceTextView.visibility = View.INVISIBLE
                    forecastTextView.visibility = View.INVISIBLE
                }

                Glide
                    .with(itemView.context)
                    .load("https://openweathermap.org/img/wn/${iconUrl}")
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean = false

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            itemView.foreCastImageLoader.visibility = View.GONE
                            return false
                        }
                    })
                    .into(itemView.locationImageView)
            }
        }
    }
}