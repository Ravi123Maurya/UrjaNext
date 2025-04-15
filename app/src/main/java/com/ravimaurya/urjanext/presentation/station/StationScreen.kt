package com.ravimaurya.urjanext.presentation.station

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.components.MyLocationFab
import com.ravimaurya.urjanext.presentation.components.NavBackTopAppBar
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationScreen(navController: NavController) {

    val carIcon = painterResource(R.drawable.ev_distance_progress)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Canvas(
           modifier = Modifier
               .fillMaxWidth()
               .height(80.dp)
               .clip(RoundedCornerShape(15.dp))
               .background(Color.Green.copy(alpha = .1f))
               .border(3.dp, Color.Green, RoundedCornerShape(15.dp))
               .clickable {  }
               .padding(30.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val radius = 8.dp.toPx()

            // Line Bar
            drawLine(
                start = Offset(0f, canvasHeight/2),
                end = Offset(canvasWidth, canvasHeight/2),
                color = Color.LightGray,
                strokeWidth = 6.dp.toPx()
            )

            // Starting Circle 1
            drawCircle(
               color = Color.Green,
                radius = radius,
                center = Offset(0f, canvasHeight/2)
            )

            // Ending Circle 2
            drawCircle(
                color = Color.Green,
                radius = radius,
                center = Offset(canvasWidth, canvasHeight/2)
            )



        }

        MyLocationFab()
    }

}






























// EVMap Screen for Demonstration


@Composable
fun EVMapScreen() {
    val viewModel: EVMapViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val evStations by viewModel.evStations.collectAsState()
    val autocompleteResults by viewModel.autocompleteResults.collectAsState()

    val cameraPositionState = rememberCameraPositionState {
        position = uiState.cameraPosition ?: CameraPosition.fromLatLngZoom(
            LatLng(0.0, 0.0), 2f
        )
    }

    Scaffold(
        topBar = { EVSearchBar(viewModel) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.loadCurrentLocation() },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Current location")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    mapType = MapType.NORMAL,
                    isMyLocationEnabled = true
                )
            ) {
                evStations.forEach { station ->
                    station.latLng?.let { latLng ->
                        Marker(
                            state = MarkerState(position = latLng),
                            title = station.name,
                            snippet = station.address,
                            icon = bitmapDescriptorFromVector(
                                LocalContext.current,
                                R.drawable.logo, //// LOgooooooooooooooooooo
                                Color(0xFF4CAF50)
                            ),
                            onClick = {
                                viewModel.onPlaceSelected(station) // Directly pass the Place object
                                true
                            }
                        )
                    }
                }
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            uiState.error?.let { error ->
                ErrorMessage(error) { viewModel.clearError() }
            }

            if (autocompleteResults.isNotEmpty()) {
                EVAutocompleteList(
                    predictions = autocompleteResults,
                    onPredictionSelected = viewModel::onPredictionSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            uiState.selectedPlace?.let { place ->
                EVStationDetailsCard(
                    place = place,
                    onDismiss = { viewModel.onPredictionSelected(null) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}

// Ev Search Bar

@Composable
fun EVSearchBar(viewModel: EVMapViewModel) {
    var searchQuery by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.onSearchQueryChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Search EV charging stations...") },
            leadingIcon = { Icon(Icons.Default.ElectricCar, contentDescription = "Search") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { viewModel.searchNearbyEVStations() }
            )
        )
    }
}

// EV Auto complete List

@Composable
fun EVAutocompleteList(
    predictions: List<AutocompletePrediction>,
    onPredictionSelected: (AutocompletePrediction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        LazyColumn {
            items(predictions) { prediction ->  // Directly use the prediction object
                ListItem(
                    headlineContent = {
                        Text(
                            text = prediction.getPrimaryText(null).toString() + " " +
                                    prediction.getSecondaryText(null).toString()
                        )
                    },
                    modifier = Modifier.clickable { onPredictionSelected(prediction) }
                )
                HorizontalDivider()
            }
        }
    }
}

// Ev Card Detail

@Composable
fun EVStationDetailsCard(
    place: Place,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(R.drawable.logo), /// change by me
                    contentDescription = "EV Station",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = place.name ?: "Unknown Station",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = place.address ?: "Address not available",
                style = MaterialTheme.typography.bodyMedium
            )

            place.rating?.let { rating ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "%.1f".format(rating),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    place.latLng?.let { latLng ->
                        val uri =
                            Uri.parse("google.navigation:q=${latLng.latitude},${latLng.longitude}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.setPackage("com.google.android.apps.maps")
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text("Navigate")
            }
        }
    }
}

// Error Message

@Composable
fun ErrorMessage(error: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = error, modifier = Modifier.weight(1f))
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Dismiss")
                }
            }
        }
    }
}

fun bitmapDescriptorFromVector(
    context: Context,
    vectorResId: Int,
    color: Color,
): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable?.setTint(color.toArgb())
    vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable!!.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}