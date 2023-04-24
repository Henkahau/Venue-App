package com.henkahau.venueapp.location

import android.content.Context
import android.location.Location
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Task
import com.henkahau.venueapp.model.UserLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.asDeferred
import javax.inject.Inject

class LocationDataProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    suspend fun getCurrentUserLocation(): UserLocation? {
        if (!LocationUtils.checkHasLocationPermission(context)) {
            return null
        }

        return fusedLocationProviderClient.getCurrentLocation(
            CurrentLocationRequest.Builder().build(),
            null
        ).toUserLocation()
    }

    private suspend fun Task<Location>.toUserLocation(): UserLocation {
        val location = asDeferred().await()
        return UserLocation(latitude = location.latitude, longitude = location.longitude)
    }
}