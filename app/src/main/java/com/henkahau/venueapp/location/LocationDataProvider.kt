package com.henkahau.venueapp.location

import android.content.Context
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.henkahau.venueapp.model.UserLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "LocationDataProvider"
private const val LOCATION_INTERVAL_MS = 10_000L
private const val MIN_UPDATE_DISTANCE_METERS = 50f

/**
 * Class providing current location.
 */
class LocationDataProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    private val locationRequest: LocationRequest =
        LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, LOCATION_INTERVAL_MS)
            .setMinUpdateDistanceMeters(MIN_UPDATE_DISTANCE_METERS)
            .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                locationDataFlow.value =
                    UserLocation(latitude = location.latitude, longitude = location.longitude)
            }
        }
    }

    private val locationDataFlow = MutableStateFlow<UserLocation?>(null)

    /**
     * Returns [UserLocation] or null whether location is available as Flow
     */
    fun getUserLocationFlow() = locationDataFlow.onEach { Log.d(TAG, "Location emitted $it") }

    /**
     * Starts requesting location updates
     */
    fun startLocationUpdates() {
        if (!LocationUtils.checkHasLocationPermission(context)) {
            Log.w(TAG, "Cannot start location updates without permissions")
            return
        }

        Log.i(TAG, "Starting location updates")
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    /**
     * Stops requesting location updates
     */
    fun stopLocationUpdates() {
        Log.i(TAG, "Location updates stopped")
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}