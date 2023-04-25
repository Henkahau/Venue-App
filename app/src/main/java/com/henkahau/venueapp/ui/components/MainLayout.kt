package com.henkahau.venueapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.henkahau.venueapp.MainViewModel
import com.henkahau.venueapp.model.VenueSearchState

/**
 * Main entry point to compose Main layout
 * with search field and [VenueList].
 */
@Composable
fun MainLayout(mainViewModel: MainViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize().padding(top = 5.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        val venues by mainViewModel.getVenues().collectAsState()
        VenueSearchLayout(venues, mainViewModel::searchVenues)
    }
}

@Composable
private fun VenueSearchLayout(
    venueSearchState: VenueSearchState,
    onTextChanged: (String) -> Unit
) {
    Column {
        Search(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            onTextChanged = onTextChanged
        )
        when (venueSearchState) {
            is VenueSearchState.Found -> {
                VenueList(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    venues = venueSearchState.venues
                )
            }

            VenueSearchState.Loading -> InfoText(info = "Loading")
            VenueSearchState.NotFound -> InfoText(info = "Not found any nearby places")
            VenueSearchState.Error -> InfoText(info = "Some problems occurred during search...")
        }
    }
}

@Composable
private fun InfoText(info: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = info, fontSize = 18.sp)
    }
}
