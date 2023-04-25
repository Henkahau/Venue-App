package com.henkahau.venueapp.model

import com.google.gson.annotations.SerializedName


/**
 * Data class representing REST API response holding [Venues].
 */
data class VenueResponse(
    @SerializedName("response") val response: Venues
)

/**
 * Data class holding list of [Venue].
 */
data class Venues(
    @SerializedName("venues") val venues: List<Venue>
)

/**
 * Data class representing single Venue.
 */
data class Venue(
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: VenueLocation
)

/**
 * Data class holding [Venue]'s location and distance.
 */
data class VenueLocation(
    @SerializedName("address") val address: String?,
    @SerializedName("distance") val distance: Int
)

/**
 * Sealed class representing status for searching process
 * of nearby venues.
 */
sealed class VenueSearchState {
    object Loading : VenueSearchState()
    object NotFound : VenueSearchState()
    object Error : VenueSearchState()
    data class Found(val venues: List<Venue>) : VenueSearchState()
}

