package com.ravimaurya.urjanext.presentation.tripplanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class TripPlannerViewModel : ViewModel() {
    // UI State
    data class UiState(
        val startLocation: String = "",
        val destination: String = "",
        val route: EvRoute? = null,
        val isLoading: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    // Fake locations database
    private val fakeLocations = listOf(
        "Central Park, New York",
        "Golden Gate Bridge, San Francisco",
        "Disneyland, Los Angeles",
        "Space Needle, Seattle"
    )

    fun updateStartLocation(query: String) {
        _uiState.update { it.copy(startLocation = query) }
    }

    fun updateDestination(query: String) {
        _uiState.update { it.copy(destination = query) }
    }

    fun swapLocations() {
        _uiState.update {
            it.copy(
                startLocation = it.destination,
                destination = it.startLocation
            )
        }
    }

    fun planTrip() {
        _uiState.update { it.copy(isLoading = true) }

        // Simulate API delay
        viewModelScope.launch {
            delay(1500) // Fake loading

            _uiState.update {
                it.copy(
                    isLoading = false,
                    route = generateDummyRoute(it.startLocation, it.destination)
                )
            }
        }
    }

    fun cancelTrip() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            delay(1500)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    route = null
                )
            }
        }
    }

    private fun generateDummyRoute(start: String, dest: String): EvRoute {
        return EvRoute(
            distance = "${(100..500).random()} km",
            duration = "${(60..240).random()} min",
            energyRequired = "${(10..50).random()} kWh",
            chargingStops = listOf(
                ChargingStop(
                    stationName = "Electrify India ${(1..5).random()}",
                    distanceFromPrevious = "${(20..100).random()} km",
                    estimatedBatteryLevel = (10..30).random(),
                    chargersAvailable = (2..6).random(),
                    chargingTime = "${(15..45).random()} min"
                ),
                ChargingStop(
                    stationName = "Urja Power ${(1..5).random()}",
                    distanceFromPrevious = "${(50..150).random()} km",
                    estimatedBatteryLevel = (40..70).random(),
                    chargersAvailable = (1..4).random(),
                    chargingTime = "${(20..60).random()} min"
                )
            )
        )
    }
}

// Data classes (same file for simplicity)
data class EvRoute(
    val distance: String,
    val duration: String,
    val energyRequired: String,
    val chargingStops: List<ChargingStop>
)

data class ChargingStop(
    val stationName: String,
    val distanceFromPrevious: String,
    val estimatedBatteryLevel: Int,
    val chargersAvailable: Int,
    val chargingTime: String
)