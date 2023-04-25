package com.henkahau.venueapp.data

import com.henkahau.venueapp.data.api.FoursquareApi
import com.henkahau.venueapp.model.UserLocation
import com.henkahau.venueapp.model.Venue
import javax.inject.Inject

/**
 * Repository class to perform data loading from [FoursquareApi].
 */
class VenueRepository @Inject constructor(
    private val api: FoursquareApi
) {
    /**
     * Returns list of nearby [Venue]s of given [location] by [query] (optional).
     */
    suspend fun getNearVenues(query: String, location: UserLocation): List<Venue>? {
        return api.getVenuesNear(
                query = query,
                coordinates = location.toCoordinatesString()
            ).let { response ->
            if (response.isSuccessful) {
                response.body()?.response?.venues
            } else {
                null
            }
        }
    }

    private fun UserLocation.toCoordinatesString(): String {
        return "$latitude,$longitude"
    }
}