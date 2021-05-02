package com.example.kausiprojektikevat.domain.model

import com.google.android.gms.maps.model.LatLng

data class Location(
    val name: String,
    val imgRef: String,
    val location: LatLng,
    val address: String
)
