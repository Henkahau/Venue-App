package com.henkahau.venueapp.data.api

import com.henkahau.venueapp.model.VenueResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Interface for Foursquare REST API calls.
 */
interface FoursquareApi {

    // TODO search by coordinates
    @Headers(ApiConstants.ACCEPT_JSON_HEADER)
    @GET(ApiConstants.SEARCH_BASE_URL + "&near=Oulunsalo")
    suspend fun getVenuesNear(
        @Query("query") query: String,
    ): Response<VenueResponse>

}