package com.henkahau.venueapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.henkahau.venueapp.data.NearVenueInfoUseCase
import com.henkahau.venueapp.model.Venue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(venueInfoUseCase: NearVenueInfoUseCase) : ViewModel() {

    private val venuesFlow = MutableStateFlow<List<Venue>>(emptyList())

    init {
        viewModelScope.launch {
            venuesFlow.value = venueInfoUseCase()
        }
    }

    /**
     * Returns list of [Venue] as State flow
     */
    fun getVenues(): StateFlow<List<Venue>> = venuesFlow

}