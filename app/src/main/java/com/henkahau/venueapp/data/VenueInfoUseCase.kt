package com.henkahau.venueapp.data

import android.util.Log
import com.henkahau.venueapp.location.LocationDataProvider
import com.henkahau.venueapp.model.Venue
import javax.inject.Inject

/**
 * Use case for loading Venue data
 */
class NearVenueInfoUseCase @Inject constructor(
    private val repository: VenueRepository,
    private val locationDataProvider: LocationDataProvider
) {

    suspend operator fun invoke(query: String): List<Venue>? {

        val location = locationDataProvider.getCurrentUserLocation() // FIXME returns null at first call
        Log.d("NearVenueInfoUseCase", "Location $location")
        return location?.let { repository.getNearVenues(query, it) }
    }
}