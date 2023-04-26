package com.henkahau.venueapp.data

import com.henkahau.venueapp.model.Venue
import com.henkahau.venueapp.model.VenueSearchState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class VenueSearchStateUseCaseTest {

    @MockK
    private lateinit var repository: VenueRepository

    private lateinit var useCase: VenueSearchStateUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = VenueSearchStateUseCase(repository)
    }

    @Test
    fun `invoke() returns Error when repository returns null`() {
        // Arrange
        coEvery { repository.getNearVenues(any(), any()) } returns null

        // Act
        val result = runBlocking {
            useCase("Something", mockk())
        }

        // Assert
        coVerify {
            repository.getNearVenues(any(), any())
        }

        assertTrue(result is VenueSearchState.Error)
    }

    @Test
    fun `invoke() returns Found when repository returns data`() {
        // Arrange
        val venue = mockk<Venue>()
        val venuesList = listOf(venue)
        coEvery { repository.getNearVenues(any(), any()) } returns venuesList

        // Act
        val result = runBlocking {
            useCase("Something", mockk())
        }

        // Assert
        coVerify {
            repository.getNearVenues(any(), any())
        }

        assertTrue(result is VenueSearchState.Found)
        assertEquals(venuesList, (result as VenueSearchState.Found).venues)
    }

    @Test
    fun `invoke() returns NotFound when repository returns empty list`() {
        // Arrange
        coEvery { repository.getNearVenues(any(), any()) } returns emptyList()

        // Act
        val result = runBlocking {
            useCase("Something", mockk())
        }

        // Assert
        coVerify {
            repository.getNearVenues(any(), any())
        }

        assertTrue(result is VenueSearchState.NotFound)
    }
}