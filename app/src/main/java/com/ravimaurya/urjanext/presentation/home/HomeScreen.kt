package com.ravimaurya.urjanext.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Parcel
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.components.AlertDialogUrja
import com.ravimaurya.urjanext.presentation.components.CircularProgressBar
import com.ravimaurya.urjanext.presentation.components.CircularProgressDialog
import com.ravimaurya.urjanext.presentation.home.urjalocation.PermissionEvent
import com.ravimaurya.urjanext.presentation.home.urjalocation.UrjaLocationViewModel
import com.ravimaurya.urjanext.presentation.home.urjalocation.ViewState
import com.ravimaurya.urjanext.presentation.home.urjalocation.hasLocationPermission
import kotlin.time.Duration.Companion.hours

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    urjaLocationViewModel: UrjaLocationViewModel = hiltViewModel(),
    isFabClicked: Boolean,
) {

    val context = LocalContext.current

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val viewState by urjaLocationViewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(!context.hasLocationPermission()) {
        permissionState.launchMultiplePermissionRequest()
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

    if (isFabClicked) {
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
                                color = Color.White
                            )
                            else Text("Settings")
                        }
                    }
                }

                is ViewState.Success -> {
                    val currentLoc =
                        LatLng(
                            location?.latitude ?: 0.0,
                            location?.longitude ?: 0.0
                        )
                    val cameraState = rememberCameraPositionState()

                    LaunchedEffect(key1 = currentLoc) {
                        cameraState.centerOnLocation(currentLoc)
                    }


                    UrjaMap(
                        currentPosition = LatLng(
                            currentLoc.latitude,
                            currentLoc.longitude
                        ),
                        cameraState = cameraState,
                        urjaLocationViewModel
                    )


                }
            }
        }
    }


}


@Composable
fun UrjaMap(
    currentPosition: LatLng,
    cameraState: CameraPositionState,
    urjaLocationViewModel: UrjaLocationViewModel,
) {

    val route by urjaLocationViewModel.route.collectAsStateWithLifecycle()
    val destination = LatLng(18.921983, 72.834656)// Example: Gateway of India


    val context = LocalContext.current
    val uiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = false,
            )
        )
    }
    val marker = LatLng(currentPosition.latitude, currentPosition.longitude)

    var onMapClickMarker by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    var isOnMapClickMarkerVisible by remember { mutableStateOf(false) }
    var onMapClickMarker2 by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    var isOnMapClickMarkerVisible2 by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                currentPosition.latitude,
                currentPosition.longitude
            ), 10f
        ) // Example: User's current Location
    }

    LaunchedEffect(Unit) {
        urjaLocationViewModel.getRoute(
            origin = currentPosition,
            destination = destination,
            apiKey = "AIzaSyAdKSH2ltnWhZgpLYXV41VmQ1wH20crGLc"
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = true,
            mapType = MapType.NORMAL,
            isTrafficEnabled = true,
        ),
        uiSettings = uiSettings,
        onMapLongClick = {
            onMapClickMarker = LatLng(it.latitude, it.longitude)
            if (isOnMapClickMarkerVisible) isOnMapClickMarkerVisible2 = true
            isOnMapClickMarkerVisible = true
        }
    ) {

        Marker(
            state = MarkerState(position = currentPosition),
            title = "Current Location",
            snippet = "You are here"
        )

        Marker(
            state = MarkerState(destination),
            title = "Destination"
        )


        if (route != null) {
            val polyline = route!!.routes[0].overviewPolyline.decodePath()
            val polylinePoints = polyline.map { LatLng(it.lat, it.lng) }

            println("Route Not Null: route: $polylinePoints")
            Polyline(
                points = polylinePoints,
                color = Color.Green,
//                visible = isOnMapClickMarkerVisible
            )
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