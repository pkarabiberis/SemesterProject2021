package com.example.kausiprojektikevat.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions

@SuppressLint("SimpleDateFormat")
fun convertToDate(time: Long): String {
    val sdf = java.text.SimpleDateFormat("EEEE, dd-MM-yyyy HH:mm")
    val date = java.util.Date(time * 1000)
    return sdf.format(date)
}

fun hasLocationPermissions(context: Context) =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    } else {
        EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }