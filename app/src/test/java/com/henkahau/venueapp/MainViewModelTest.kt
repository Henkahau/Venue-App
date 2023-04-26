package com.henkahau.venueapp

import androidx.lifecycle.LifecycleOwner
import com.henkahau.venueapp.data.VenueSearchStateUseCase
import com.henkahau.venueapp.location.LocationDataProvider
import com.henkahau.venueapp.model.UserLocation
import com.henkahau.venueapp.model.Venue
import com.henkahau.venueapp.model.VenueSearchState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @MockK
    private lateinit var useCase: VenueSearchStateUseCase

    @MockK(relaxUnitFun = true)
    private lateinit var locationDataProvider: LocationDataProvider

    @MockK
    private lateinit var defaultLocation: UserLocation

    private lateinit var viewModel: MainViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        every { locationDataProvider.getUserLocationFlow() } returns flowOf(defaultLocation)
        coEvery { useCase(any(), any()) } returns VenueSearchState.NotFound
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `getVenues() returns flow of data from useCase by location when no search words given`() =
        runTest {
            // Arrange
            val venue = mockk<Venue>()
            val venuesList = listOf(venue)
            val state = VenueSearchState.Found(venuesList)

            coEvery { useCase(any(), defaultLocation) } returns state

            viewModel = MainViewModel(useCase, locationDataProvider)

            // Act
            val result = viewModel.getVenues().value

            // Assert
            assertEquals(state, result)
        }

    @Test
    fun `getVenues() returns flow of data from useCase by location when search word is given by searchVenues()`() =
        runTest {
            // Arrange
            val query = "Some query"
            val venue = mockk<Venue>()
            val venuesList = listOf(venue)
            val state = VenueSearchState.Found(venuesList)

            val updatedState = VenueSearchState.NotFound

            coEvery { useCase("", defaultLocation) } returns state
            coEvery { useCase(query, defaultLocation) } returns updatedState

            viewModel = MainViewModel(useCase, locationDataProvider)

            // Act
            viewModel.searchVenues(query)
            val result = viewModel.getVenues().value

            // Assert
            coVerify {
                useCase("", defaultLocation)
                useCase(query, defaultLocation)
            }

            assertEquals(updatedState, result)

            confirmVerified(useCase)
        }

    @Test
    fun `getVenues() returns Loading state when location is not available`() =
        runTest {
            // Arrange
            every { locationDataProvider.getUserLocationFlow() } returns flowOf(null)
            viewModel = MainViewModel(useCase, locationDataProvider)

            // Act
            val result = viewModel.getVenues().value

            // Assert
            assertTrue(result is VenueSearchState.Loading)
        }

    @Test
    fun `onResume lifecycle event starts location updates`() {
        // Arrange
        val lifeCycleOwner = mockk<LifecycleOwner>()
        viewModel = MainViewModel(useCase, locationDataProvider)

        // Act
        viewModel.onResume(lifeCycleOwner)

        // Assert
        verify {
            locationDataProvider.startLocationUpdates()
        }
    }

    @Test
    fun `onPause lifecycle event stops location updates when if started`() {
        // Arrange
        val lifeCycleOwner = mockk<LifecycleOwner>()
        viewModel = MainViewModel(useCase, locationDataProvider)
        viewModel.onResume(lifeCycleOwner)

        // Act
        viewModel.onPause(lifeCycleOwner)

        // Assert
        verify {
            locationDataProvider.stopLocationUpdates()
        }
    }
}