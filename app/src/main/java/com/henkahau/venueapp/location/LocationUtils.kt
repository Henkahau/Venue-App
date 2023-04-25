package com.henkahau.venueapp.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat

object LocationUtils {

    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    /**
     * Checks whether location permissions are granted or not.
     */
    fun checkHasLocationPermission(context: Context): Boolean {
        return locationPermissions.all { ContextCompat.checkSelfPermission(context, it) == PERMISSION_GRANTED }
    }
}