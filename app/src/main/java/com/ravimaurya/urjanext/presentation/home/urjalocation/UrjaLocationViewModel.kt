package com.ravimaurya.urjanext.presentation.home.urjalocation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.S)
@HiltViewModel
class UrjaLocationViewModel @Inject constructor(
    private val getLocationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    private val _route = MutableStateFlow<DirectionsResult?>(null)
    val route: StateFlow<DirectionsResult?> = _route.asStateFlow()

    fun getRoute(origin: LatLng, destination: LatLng, apiKey: String){
        viewModelScope.launch {
            try {
                val context = GeoApiContext
                    .Builder()
                    .apiKey(apiKey)
                    .build()

                val directionsResult = DirectionsApi.newRequest(context)
                    .origin("${origin.latitude}, ${origin.longitude}")
                    .destination("${destination.latitude}, ${destination.longitude}")
                    .mode(TravelMode.DRIVING)
                    .await()

                println("directionResultFound: ${directionsResult}")
                _route.value = directionsResult
            } catch (e: Exception){
                println("Routing Error:  Error Getting Route ${e.message}")
            }
        }
    }

    fun handle(event: PermissionEvent){
        when(event){
            PermissionEvent.Granted -> {
                viewModelScope.launch {
                    getLocationUseCase.invoke()
                        .collect{ location ->
                            _viewState.value = ViewState.Success(location)
                        }
                }
            }
            PermissionEvent.Revoked -> {
                _viewState.value = ViewState.RevokedPermissions
            }
        }
    }

}


sealed interface ViewState {
    object Loading : ViewState
    data class Success(val location: LatLng?) : ViewState
    object RevokedPermissions : ViewState
}

sealed interface PermissionEvent {
    object Granted : PermissionEvent
    object Revoked : PermissionEvent
}