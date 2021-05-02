package com.example.kausiprojektikevat.data.network.model.location

data class Location(
    val name: String = "",
    val imgRef: String? = null,
    val lat: Double? = null,
    val lon: Double? = null,
    val address: String = "",
    val distance: String? = null
)