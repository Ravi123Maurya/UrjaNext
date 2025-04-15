package com.ravimaurya.urjanext.presentation.station

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EVMapViewModel @Inject constructor(
    private val placesClient: PlacesClient,
    private val locationClient: LocationClient,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(EVMapUiState())
    val uiState: StateFlow<EVMapUiState> = _uiState.asStateFlow()

    private val _evStations = MutableStateFlow<List<Place>>(emptyList())
    val evStations: StateFlow<List<Place>> = _evStations.asStateFlow()

    private val _autocompleteResults = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val autocompleteResults: StateFlow<List<AutocompletePrediction>> = _autocompleteResults.asStateFlow()

    init {
        loadCurrentLocation()
    }

    fun loadCurrentLocation() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val location = locationClient.getCurrentLocation()
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    _uiState.update { state ->
                        state.copy(
                            currentLocation = latLng,
                            cameraPosition = CameraPosition.fromLatLngZoom(latLng, 12f),
                            isLoading = false
                        )
                    }
                    searchNearbyEVStations(latLng)
                } ?: run {
                    _uiState.update { it.copy(isLoading = false, error = "Location permission required") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun searchNearbyEVStations(location: LatLng? = null) {
        _uiState.update { it.copy(isLoading = true) }
        val searchLocation = location ?: _uiState.value.currentLocation ?: return

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.TYPES
        )

        val request = FindCurrentPlaceRequest.newInstance(placeFields)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            _uiState.update { it.copy(isLoading = false, error = "Location permission required") }
            return
        }

        placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
            val stations = response.placeLikelihoods
                .filter { likelihood ->
                    val types = likelihood.place.types.orEmpty()
                    types.any { type ->
                        type.equals("charging_station") ||
                                type.equals("electric_vehicle_charging_station")
                    }
                }
                .sortedByDescending { it.likelihood }
                .map { it.place }

            _evStations.value = stations
            _uiState.update {
                it.copy(
                    isLoading = false,
                    cameraPosition = CameraPosition.fromLatLngZoom(searchLocation, 12f)
                )
            }
        }.addOnFailureListener { exception ->
            _uiState.update { it.copy(isLoading = false, error = exception.message) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        if (query.length > 2) {
            val builder = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setTypesFilter(listOf("charging_station"))

            _uiState.value.currentLocation?.let { location ->
                builder.setLocationBias(
                    RectangularBounds.newInstance(
                    LatLng(location.latitude - 0.1, location.longitude - 0.1),
                    LatLng(location.latitude + 0.1, location.longitude + 0.1)
                ))
            }

            placesClient.findAutocompletePredictions(builder.build())
                .addOnSuccessListener { response ->
                    _autocompleteResults.value = response.autocompletePredictions
                }.addOnFailureListener { exception ->
                    _uiState.update { it.copy(error = exception.message) }
                }
        } else {
            _autocompleteResults.value = emptyList()
        }
    }

    fun onPlaceSelected(place: Place) {
        place.latLng?.let { latLng ->
            _uiState.update {
                it.copy(
                    cameraPosition = CameraPosition.fromLatLngZoom(latLng, 15f),
                    selectedPlace = place
                )
            }
            // Optional: Search for nearby stations around the selected place
            searchNearbyEVStations(latLng)
        }
        // Clear any autocomplete results
        _autocompleteResults.value = emptyList()
    }

    fun onPredictionSelected(prediction: AutocompletePrediction?) {
        if (prediction == null) {
            _uiState.update { it.copy(selectedPlace = null) }
            return
        }

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.RATING,
            Place.Field.OPENING_HOURS,
            Place.Field.TYPES
        )

        val request = FetchPlaceRequest.newInstance(prediction.placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                onPlaceSelected(response.place) // Reuse the place selection logic
            }.addOnFailureListener { exception ->
                _uiState.update { it.copy(error = exception.message) }
            }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

data class EVMapUiState(
    val currentLocation: LatLng? = null,
    val cameraPosition: CameraPosition? = null,
    val selectedPlace: Place? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)