package com.ravimaurya.urjanext.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.ravimaurya.urjanext.domain.model.EVStation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EVSearchViewModel @Inject constructor(
    private val placesClient: PlacesClient
) : ViewModel() {

    private val _isSearchClicked = MutableStateFlow<Boolean>(false)
    val isSearchClicked: StateFlow<Boolean> = _isSearchClicked.asStateFlow()

    fun searchClickedOrDismiss(){
        _isSearchClicked.value = !_isSearchClicked.value
    }

    fun searchEVStations(
        query: String,
        onResult: (List<EVStation>) -> Unit,
    ) = viewModelScope.launch {

        if (query.length < 2) return@launch

        println("Searching.........")
        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setCountries("In")
//            .setTypesFilter(listOf("ev_charging_station", "charging_station")) // Fix filter
            .build()
        println("AutoCompletePrediction Request done ; $request")
        placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response ->
                println("PLaces Client Response : $response")
                val predictions = response.autocompletePredictions
                val evStations = mutableListOf<EVStation>()

                predictions.forEach { prediction -> // Loop through results
                    val placeId = prediction.placeId
                    val placeFields = listOf(
                        Place.Field.DISPLAY_NAME,
                        Place.Field.LOCATION,
                        Place.Field.NAME,
                        Place.Field.ADDRESS,
                        Place.Field.ID
                    )

                    val fetchRequest = FetchPlaceRequest.builder(placeId, placeFields).build()
                    placesClient.fetchPlace(fetchRequest)
                        .addOnSuccessListener { placeResponse ->
                            val place = placeResponse.place
                            println("Place Response : ${place.displayName} : ${place.id}")
                            place.location?.let { location ->
                                println("Location : $location")
                                val station = EVStation(
                                    name = place.displayName ?: "Unknown Station",
                                    address = place.address ?: "Unknown Address",
                                    location = location,
                                    availableChargers = (0..10).random() // Provide default or fetch from another API
                                )
                                evStations.add(station)
                                println("Station found @ ${station.location} ${station.name}")

                            }
                            onResult(evStations)
                            println("Evstations : $evStations")
                        }
                        .addOnFailureListener {
                            println("Failed to fetch EV station details")
                        }
                }
            }
    }

}

