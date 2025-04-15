package com.ravimaurya.urjanext.presentation.station

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val placesClient by lazy {
        Places.createClient(context)
    }

    @SuppressLint("MissingPermission")
    fun searchNearbyEVStations(
        location: LatLng,
        radius: Int,
        onSuccess: (List<Place>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val bias = RectangularBounds.newInstance(
            LatLng(location.latitude - 0.1, location.longitude - 0.1),
            LatLng(location.latitude + 0.1, location.longitude + 0.1)
        )

        val request = FindCurrentPlaceRequest.newInstance(
            listOf(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.TYPES)
        )

        val placeFields = listOf(
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.TYPES
        )

        val request2 = FindCurrentPlaceRequest.newInstance(placeFields)

        placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
            val evStations = response.placeLikelihoods
//                .filter { likelihood ->
//                    likelihood.place.types?.contains("charging_station") == true
//                }
                .filter { likelihood ->
                    val types = likelihood.place.types.orEmpty()
                    types.any { type ->
                        type.equals("charging_station") == true
                    }
                }
                .sortedByDescending { it.likelihood }
                .map { it.place }

            onSuccess(evStations)
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }

    fun autocompleteEVStations(
        query: String,
        location: LatLng? = null,
        onSuccess: (List<AutocompletePrediction>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val builder = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .setTypesFilter(listOf("charging_station"))

        location?.let {
            builder.setLocationBias(RectangularBounds.newInstance(
                LatLng(it.latitude - 0.1, it.longitude - 0.1),
                LatLng(it.latitude + 0.1, it.longitude + 0.1)
            ))
        }

        placesClient.findAutocompletePredictions(builder.build())
            .addOnSuccessListener { response ->
                onSuccess(response.autocompletePredictions)
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun getPlaceDetails(
        placeId: String,
        onSuccess: (Place) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS,
            Place.Field.RATING,
            Place.Field.OPENING_HOURS,
            Place.Field.PHONE_NUMBER
        )

        val request = FetchPlaceRequest.newInstance(placeId, placeFields)

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                onSuccess(response.place)
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}