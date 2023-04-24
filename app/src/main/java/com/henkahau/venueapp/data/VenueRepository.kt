package com.henkahau.venueapp.data

import com.henkahau.venueapp.data.api.FoursquareApi
import com.henkahau.venueapp.model.UserLocation
import com.henkahau.venueapp.model.Venue
import javax.inject.Inject

/**
 * Repository class to perform data loading from
 */
class VenueRepository @Inject constructor(
    private val api: FoursquareApi
) {

    suspend fun getNearVenues(query: String, location: UserLocation): List<Venue> {
        return api.getVenuesNear(
            query
        ).body()?.response?.venues ?: emptyList()
    }
}