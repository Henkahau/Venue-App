package com.henkahau.venueapp.data

import com.henkahau.venueapp.data.api.FoursquareApi
import com.henkahau.venueapp.model.UserLocation
import com.henkahau.venueapp.model.Venue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class VenueRepositoryTest {

   @MockK
   private lateinit var api: FoursquareApi

   private lateinit var repository: VenueRepository

    private val givenLocation = UserLocation(latitude = 123.0, longitude = 321.0)
    private val queryString = "Some text"
    private val expectedLocationQuery = "${givenLocation.latitude},${givenLocation.longitude}"

    @Before
   fun setUp() {
       MockKAnnotations.init(this)
       repository = VenueRepository(api = api)
   }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getNearVenues() returns found data via FoursquareApi`() {
        // Arrange
        val venuesList = mockk<List<Venue>>()

        coEvery { api.getVenuesNear(any(), any()) } returns mockk {
            every { isSuccessful } returns true
            every { body()?.response?.venues } returns venuesList
        }

        // Act
        val result = runBlocking {
            repository.getNearVenues(
                query = queryString,
                location = givenLocation
            )
        }

        // Assert
        coVerify {
            api.getVenuesNear(queryString, expectedLocationQuery)
        }

        assertEquals(venuesList, result)
    }

    @Test
    fun `getNearVenues() returns null when the request is not successful`() {
        // Arrange
        coEvery { api.getVenuesNear(any(), any()) } returns mockk {
            every { isSuccessful } returns false
        }

        val result = runBlocking {
            repository.getNearVenues(
                query = queryString,
                location = givenLocation
            )
        }

        // Assert
        coVerify {
            api.getVenuesNear(queryString, expectedLocationQuery)
        }

        assertNull(result)
    }

    @Test
    fun `getNearVenues() returns null when the request body is null`() {
        // Arrange
        coEvery { api.getVenuesNear(any(), any()) } returns mockk {
            every { isSuccessful } returns true
            every { body() } returns null
        }

        val result = runBlocking {
            repository.getNearVenues(
                query = queryString,
                location = givenLocation
            )
        }

        // Assert
        coVerify {
            api.getVenuesNear(queryString, expectedLocationQuery)
        }

        assertNull(result)
    }

    @Test
    fun `getNearVenues() returns null when the request venue response is null`() {
        // Arrange
        coEvery { api.getVenuesNear(any(), any()) } returns mockk {
            every { isSuccessful } returns true
            every { body()?.response } returns null
        }

        val result = runBlocking {
            repository.getNearVenues(
                query = queryString,
                location = givenLocation
            )
        }

        // Assert
        coVerify {
            api.getVenuesNear(queryString, expectedLocationQuery)
        }

        assertNull(result)
    }
}