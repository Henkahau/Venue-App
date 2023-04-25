package com.henkahau.venueapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.henkahau.venueapp.model.Venue
import com.henkahau.venueapp.model.VenueLocation

/**
 * Main entry point to compose vertical list of [venues].
 */
@Composable
fun VenueList(modifier: Modifier = Modifier, venues: List<Venue>) {
    Column(modifier = modifier) {
        Text(modifier = Modifier.padding(start = 5.dp), text = "Places near you:")
        LazyColumn {
            items(venues) { venue ->
                VenueComponent(venue = venue)
            }
        }
    }
}

@Composable
private fun VenueComponent(modifier: Modifier = Modifier, venue: Venue) {
    Card(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(text = venue.name)
            Text(text = "Address: ${venue.location.address ?: ""}")
            Text(text = "Distance: ${venue.location.distance} meters")
        }
    }
}

@Preview
@Composable
private fun PreviewVenueList() {
    val venues = listOf(
        Venue(
            name = "Some place",
            location = VenueLocation(
                address = "SomeStreet 2",
                distance = 0
            )
        ),
        Venue(
            name = "Some other place",
            location = VenueLocation(
                address = "SomeStreet 3",
                distance = 5
            )
        )
    )
    Surface(modifier = Modifier.fillMaxSize()) {
        VenueList(venues = venues)
    }
}