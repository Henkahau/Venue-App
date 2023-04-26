package com.henkahau.venueapp.data

import com.henkahau.venueapp.model.UserLocation
import com.henkahau.venueapp.model.VenueSearchState
import javax.inject.Inject

/**
 * Use case for loading [VenueSearchState].
 */
class VenueSearchStateUseCase @Inject constructor(
    private val repository: VenueRepository
) {

    suspend operator fun invoke(query: String, userLocation: UserLocation): VenueSearchState {
        val result = repository.getNearVenues(query, userLocation)

        return when {
            result == null -> VenueSearchState.Error
            result.isNotEmpty() -> VenueSearchState.Found(result)
            else -> VenueSearchState.NotFound
        }
    }
}