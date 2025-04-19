package com.ravimaurya.urjanext.presentation.home.urjalocation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

interface ILocationService {
    fun requestLocationUpdates(): Flow<LatLng?>
    fun requestCurrentLocation(): Flow<LatLng?>
}

class LocationService @Inject constructor(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient
): ILocationService {
    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override fun requestLocationUpdates(): Flow<LatLng?> = callbackFlow {

        if (!context.hasLocationPermission()) {
            trySend(null)
            return@callbackFlow
        }

        val request = LocationRequest.Builder(10000L)
            .setIntervalMillis(10000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let {
                    trySend(LatLng(it.latitude, it.longitude))
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun requestCurrentLocation(): Flow<LatLng?> {
        TODO("Not yet implemented")
    }

}


fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}


class GetLocationUseCase @Inject constructor(
    private val locationService: ILocationService
) {
    @RequiresApi(Build.VERSION_CODES.S)
    operator fun invoke(): Flow<LatLng?> = locationService.requestLocationUpdates()

}



// Camera Position Saver
object CameraPositionSaver : Saver<CameraPositionState, List<Any>> {
    override fun restore(value: List<Any>): CameraPositionState {
        val latitude = value[0] as Double
        val longitude = value[1] as Double
        val zoom = value[2] as Float

        return CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                LatLng(latitude, longitude),
                zoom
            )
        )
    }



    override fun SaverScope.save(value: CameraPositionState): List<Any> {
        val position = value.position
        return listOf(
            position.target.latitude,
            position.target.longitude,
            position.zoom
        )
    }

}

// Map UI Settings Saver
object MapUiSettingsSaver : Saver<MapUiSettings, Map<String, Boolean>> {
    override fun restore(value: Map<String, Boolean>): MapUiSettings {
        return MapUiSettings(
            compassEnabled = value["compassEnabled"] ?: true,
            indoorLevelPickerEnabled = value["indoorLevelPickerEnabled"] ?: true,
            mapToolbarEnabled = value["mapToolbarEnabled"] ?: true,
            myLocationButtonEnabled = value["myLocationButtonEnabled"] ?: false,
            rotationGesturesEnabled = value["rotationGesturesEnabled"] ?: true,
            scrollGesturesEnabled = value["scrollGesturesEnabled"] ?: true,
            scrollGesturesEnabledDuringRotateOrZoom = value["scrollGesturesEnabledDuringRotateOrZoom"] ?: true,
            tiltGesturesEnabled = value["tiltGesturesEnabled"] ?: true,
            zoomControlsEnabled = value["zoomControlsEnabled"] ?: true,
            zoomGesturesEnabled = value["zoomGesturesEnabled"] ?: true
        )
    }

    override fun SaverScope.save(value: MapUiSettings): Map<String, Boolean> {
        return mapOf(
            "compassEnabled" to value.compassEnabled,
            "indoorLevelPickerEnabled" to value.indoorLevelPickerEnabled,
            "mapToolbarEnabled" to value.mapToolbarEnabled,
            "myLocationButtonEnabled" to value.myLocationButtonEnabled,
            "rotationGesturesEnabled" to value.rotationGesturesEnabled,
            "scrollGesturesEnabled" to value.scrollGesturesEnabled,
            "scrollGesturesEnabledDuringRotateOrZoom" to value.scrollGesturesEnabledDuringRotateOrZoom,
            "tiltGesturesEnabled" to value.tiltGesturesEnabled,
            "zoomControlsEnabled" to value.zoomControlsEnabled,
            "zoomGesturesEnabled" to value.zoomGesturesEnabled
        )
    }
}

// Map Properties Saver
object MapPropertiesSaver : Saver<MapProperties, Map<String, Any>> {
    override fun restore(value: Map<String, Any>): MapProperties {
        return MapProperties(
            isBuildingEnabled = value["isBuildingEnabled"] as? Boolean ?: true,
            isIndoorEnabled = value["isIndoorEnabled"] as? Boolean ?: true,
            isMyLocationEnabled = value["isMyLocationEnabled"] as? Boolean ?: true,
            isTrafficEnabled = value["isTrafficEnabled"] as? Boolean ?: true,
            mapType = (value["mapType"] as? Int)?.let { intValue ->
                when (intValue) {
                    GoogleMap.MAP_TYPE_SATELLITE -> MapType.SATELLITE
                    GoogleMap.MAP_TYPE_TERRAIN -> MapType.TERRAIN
                    GoogleMap.MAP_TYPE_HYBRID -> MapType.HYBRID
                    GoogleMap.MAP_TYPE_NONE -> MapType.NONE
                    else -> MapType.NORMAL
                }
            } ?: MapType.NORMAL,
            maxZoomPreference = (value["maxZoomPreference"] as? Float) ?: 21f,
            minZoomPreference = (value["minZoomPreference"] as? Float) ?: 0f,
            latLngBoundsForCameraTarget = value["latLngBoundsForCameraTarget"] as? LatLngBounds
        )
    }

    override fun SaverScope.save(value: MapProperties): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "isBuildingEnabled" to value.isBuildingEnabled,
            "isIndoorEnabled" to value.isIndoorEnabled,
            "isMyLocationEnabled" to value.isMyLocationEnabled,
            "isTrafficEnabled" to value.isTrafficEnabled,
            "mapType" to value.mapType,
            "maxZoomPreference" to value.maxZoomPreference,
            "minZoomPreference" to value.minZoomPreference
        )

        // Only add latLngBoundsForCameraTarget if it's not null
        value.latLngBoundsForCameraTarget?.let { bounds ->
            map["latLngBoundsForCameraTarget"] = bounds
        }

        return map
    }
}