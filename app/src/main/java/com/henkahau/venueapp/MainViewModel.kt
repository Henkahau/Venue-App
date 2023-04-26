package com.henkahau.venueapp

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henkahau.venueapp.data.VenueSearchStateUseCase
import com.henkahau.venueapp.location.LocationDataProvider
import com.henkahau.venueapp.model.Venue
import com.henkahau.venueapp.model.VenueSearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val venueSearchStateUseCase: VenueSearchStateUseCase,
    private val locationDataProvider: LocationDataProvider
) : ViewModel(), DefaultLifecycleObserver {

    private val venuesFlow = MutableStateFlow<VenueSearchState>(VenueSearchState.Loading)
    private val queryFlow = MutableStateFlow("")
    private var isRequestingLocation = false

    init {
        viewModelScope.launch {
            combine(locationDataProvider.getUserLocationFlow().filterNotNull(), queryFlow) { location, query ->
                Pair(location, query)
            }.collectLatest { (location, query) ->
                venuesFlow.value = venueSearchStateUseCase(query, location)
            }
        }
    }

    /**
     * Returns list of [Venue] as State flow
     */
    fun getVenues(): StateFlow<VenueSearchState> = venuesFlow

    /**
     * Search nearby venues by [query]
     */
    fun searchVenues(query: String) {
        queryFlow.value = query
    }

    override fun onResume(owner: LifecycleOwner) {
        if(!isRequestingLocation) {
            locationDataProvider.startLocationUpdates()
            isRequestingLocation = true
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        if (isRequestingLocation) {
            locationDataProvider.stopLocationUpdates()
            isRequestingLocation = false
        }
    }
}