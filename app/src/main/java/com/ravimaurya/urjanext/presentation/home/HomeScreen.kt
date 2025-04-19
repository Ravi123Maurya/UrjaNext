package com.ravimaurya.urjanext.presentation.home

import android.Manifest
import android.content.Intent
import android.icu.text.StringSearch
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.ravimaurya.urjanext.BuildConfig
import com.ravimaurya.urjanext.domain.model.EVStation
import com.ravimaurya.urjanext.presentation.components.ChargingStationCard
import com.ravimaurya.urjanext.presentation.components.ChargingStationDetailCard
import com.ravimaurya.urjanext.presentation.components.CircularProgressBar
import com.ravimaurya.urjanext.presentation.components.DemoSearchBar
import com.ravimaurya.urjanext.presentation.components.MyLocationFab
import com.ravimaurya.urjanext.presentation.home.urjalocation.CameraPositionSaver
import com.ravimaurya.urjanext.presentation.home.urjalocation.MapPropertiesSaver
import com.ravimaurya.urjanext.presentation.home.urjalocation.MapUiSettingsSaver
import com.ravimaurya.urjanext.presentation.home.urjalocation.PermissionEvent
import com.ravimaurya.urjanext.presentation.home.urjalocation.UrjaLocationViewModel
import com.ravimaurya.urjanext.presentation.home.urjalocation.ViewState
import com.ravimaurya.urjanext.presentation.home.urjalocation.hasLocationPermission
import com.ravimaurya.urjanext.presentation.station.EVMapViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    urjaLocationViewModel: UrjaLocationViewModel = hiltViewModel(),
    evSearchViewModel: EVSearchViewModel = hiltViewModel(),
    isFabClicked: Boolean,
    isSearchClicked: Boolean,
) {

    val context = LocalContext.current

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val viewState by urjaLocationViewModel.viewState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }
    var evStations by remember { mutableStateOf(emptyList<EVStation>()) }
    var searchJob by remember { mutableStateOf<Job?>(null) }
    var currentLoc by remember { mutableStateOf<LatLng>(LatLng(18.921983, 72.834656)) }
    // Map Search Query
    LaunchedEffect(searchQuery) {
        searchJob?.cancel() // Cancel the previous search if a new one starts
        searchJob = launch {
            delay(300) // Debounce to prevent too many API calls
            evStations = emptyList()
            evSearchViewModel.searchEVStations(searchQuery) { stations ->
                evStations =
                    evStations + stations.filter { it !in evStations } // Replace the existing list with fresh results
            }
        }
    }

    // Location Permission
    LaunchedEffect(!context.hasLocationPermission()) {
        permissionState.launchMultiplePermissionRequest()
    }

    val cameraState = rememberCameraPositionState()

    LaunchedEffect(key1 = currentLoc) {
        currentLoc.let {
            cameraState.centerOnLocation(it)
        }
    }

    when {
        permissionState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                urjaLocationViewModel.handle(PermissionEvent.Granted)
            }
        }

        permissionState.shouldShowRationale -> {
            // Take a look
            RationaleAlert(onDismiss = {}) {
                permissionState.launchMultiplePermissionRequest()
            }
        }

        !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
            LaunchedEffect(Unit) {
                urjaLocationViewModel.handle(PermissionEvent.Revoked)
            }
        }
    }

    with(viewState) {
        when (this) {
            ViewState.Loading -> {
                CircularProgressBar(true)
            }

            ViewState.RevokedPermissions -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("We need permissions to use this app")
                    Button(
                        onClick = {

                            startActivity(
                                context,
                                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                null
                            )
                        },
                        enabled = !context.hasLocationPermission()
                    ) {
                        if (context.hasLocationPermission()) CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = Color.Red
                        )
                        else Text("Settings")
                    }
                }
            }

            is ViewState.Success -> {
                currentLoc =
                    LatLng(
                        location?.latitude ?: 0.0,
                        location?.longitude ?: 0.0
                    )


            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedVisibility(isSearchClicked) {
//                            EVStationSearchBar() { query ->
//                                searchQuery = query
//                            }
            DemoSearchBar(stationPrediction = evStations) { query ->
                searchQuery = query
            }
        }

        UrjaMap(
            currentPosition = LatLng(
                currentLoc.latitude,
                currentLoc.longitude
            ),
            cameraState = cameraState,
            urjaLocationViewModel,
            evSearchViewModel,
            isSearchClicked
        )
    }


}


@Composable
fun UrjaMap(
    currentPosition: LatLng,
    cameraState: CameraPositionState,
    urjaLocationViewModel: UrjaLocationViewModel,
    evSearchViewModel: EVSearchViewModel,
    isSearchClicked: Boolean
) {

    val route by urjaLocationViewModel.route.collectAsStateWithLifecycle()
    val destination = LatLng(18.921983, 72.834656)// Example: Gateway of India
    val mapApiKey = BuildConfig.MAPS_API_KEY

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiSettings = rememberSaveable(
        saver = MapUiSettingsSaver
    ) {

        MapUiSettings(
            compassEnabled = true,
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false
        )

    }
    val mapProperties = rememberSaveable(
        saver = MapPropertiesSaver
    ) {

        MapProperties(
            isBuildingEnabled = true,
            isMyLocationEnabled = true,
            mapType = MapType.NORMAL,
            isTrafficEnabled = true,
        )

    }
    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)

    var onMapClickMarker by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    var isOnMapClickMarkerVisible by remember { mutableStateOf(false) }

    var isOnMapClickMarkerVisible2 by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                currentPosition.latitude,
                currentPosition.longitude
            ), 15f
        ) // Example: User's current Location
    }

    val cameraPositionState2 = rememberSaveable(
        saver = CameraPositionSaver
    ) {
        CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                LatLng(currentPosition.latitude, currentPosition.latitude), // Default position
                10f // Default zoom
            )
        )
    }

    var isChargingStationMarkerClicked by remember { mutableStateOf(false) }
    var isContinueClicked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        urjaLocationViewModel.getRoute(
            origin = currentPosition,
            destination = destination,
            apiKey = mapApiKey
        )
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Google Map
        GoogleMap(
            cameraPositionState = cameraPositionState2,
            properties = mapProperties,
            uiSettings = uiSettings,
            onMapClick = {
                isChargingStationMarkerClicked = false
                isContinueClicked = false
            },
            onMapLongClick = {
                onMapClickMarker = LatLng(it.latitude, it.longitude)
                if (isOnMapClickMarkerVisible) isOnMapClickMarkerVisible2 = true
                isOnMapClickMarkerVisible = true
            }
        ) {
            Marker(
                state = MarkerState(currentPosition),
                onClick = { mkr ->
                    isChargingStationMarkerClicked = !isChargingStationMarkerClicked
                    true
                }
            )

        }

        // MyLocation Camera FAB Button
        MyLocationFab(
            modifier = Modifier
                .align(Alignment.TopEnd),
            onClick = {
                println("Current Position2 : ${currentPosition.latitude}, ${currentPosition.longitude}")
                currentPosition?.let {
                    scope.launch {
                        cameraPositionState2.animate(
                            CameraUpdateFactory.newLatLngZoom(it, 17f)
                        )
                        println("Current Position : ${it.latitude}, ${it.longitude}")
                    }
                }
            },
            hasLocationPermission = context.hasLocationPermission()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            // Charging Station Detail
            AnimatedVisibility(
                visible = isChargingStationMarkerClicked && !isSearchClicked,
                enter = slideInVertically(initialOffsetY = { it }),  // Slide in from bottom
                exit = if(isContinueClicked) fadeOut() else slideOutVertically(targetOffsetY = { it })    // Slide out to bottom
            ) {
                ChargingStationCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onContinueClick = {
                        isChargingStationMarkerClicked = false
                        isContinueClicked = true
                    }
                )
            }

            AnimatedVisibility(
                visible = isContinueClicked,
                enter = slideInVertically(initialOffsetY = { it }),  // Slide in from bottom
                exit = if(isContinueClicked) fadeOut() else slideOutVertically(targetOffsetY = { it })
            ) {
                ChargingStationDetailCard()
            }
        }
    }

}

@Composable
fun RationaleAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "We need location permissions to use this app",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }
    }
}


private suspend fun CameraPositionState.centerOnLocation(
    location: LatLng,
) = animate(
    update = CameraUpdateFactory.newLatLngZoom(
        location,
        15f
    ),
    durationMs = 1500
)
