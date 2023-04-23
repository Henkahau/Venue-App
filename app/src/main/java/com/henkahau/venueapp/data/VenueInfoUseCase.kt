package com.henkahau.venueapp.data

import com.henkahau.venueapp.model.Venue
import javax.inject.Inject

/**
 * Use case for loading Venue data
 */
class NearVenueInfoUseCase @Inject constructor(
    private val repository: VenueRepository // TODO add location provider
) {

    suspend operator fun invoke(): List<Venue> {
        return repository.getNearVenues()
    }
}