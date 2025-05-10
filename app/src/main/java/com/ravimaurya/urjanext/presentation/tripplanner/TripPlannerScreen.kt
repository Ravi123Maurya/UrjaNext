package com.ravimaurya.urjanext.presentation.tripplanner

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.components.BigButton
import com.ravimaurya.urjanext.presentation.components.OutlinedInputField
import com.ravimaurya.urjanext.presentation.components.UrjaSearchScreen
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import com.ravimaurya.urjanext.theme.Green40


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPlannerScreen(
    navController: NavController,
    mainNavController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TripPlannerViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var hasDestClicked by remember { mutableStateOf(false) }
    var hasStartClicked by remember { mutableStateOf(false) }


    UrjaSearchScreen(
        isVisible = hasDestClicked || hasStartClicked,
        onDismiss = {
            hasStartClicked = false
            hasDestClicked = false
        },
        onSearchQueryChanged = {

        },
        onLocationSelected = { evStation ->
            println("Address: ${evStation.address}")
            evStation.address?.let {
                println("Address: $it")
                if (hasStartClicked) viewModel.updateStartLocation(it) else viewModel.updateDestination(it)
            }
            hasStartClicked = false
            hasDestClicked = false
        }
    )


    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // Location Input
        LocationInputSection(
            mainNavController = mainNavController,
            startLocation = uiState.startLocation,
            destination = uiState.destination,
            onStartChange = viewModel::updateStartLocation,
            onDestChange = viewModel::updateDestination,
            onSwap = viewModel::swapLocations,
            isStartLocationClicked = {
                hasStartClicked = true
            },
            isDestinationClicked = {
                hasDestClicked = true
            }
        )

        Spacer(Modifier.height(16.dp))

        // Results
        if (uiState.route != null) {
            RouteResultsSection(route = uiState.route!!)
        }
        if (uiState.isLoading) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF05C93C))
            }
        }


        // Plan Button
        Row(
            Modifier.wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Plan or Cancel 'Trip' Button
            BigButton(
                label = if (uiState.route == null) R.string.plan_trip else R.string.cancel_trip,
                onClick = if (uiState.route == null) viewModel::planTrip else viewModel::cancelTrip,
                modifier = Modifier
                    .fillMaxWidth(if (uiState.route == null) 1f else 0.6f)
                    .padding(top = 16.dp),
                enabled = uiState.startLocation.isNotEmpty() &&
                        uiState.destination.isNotEmpty(),

                )

            Spacer(Modifier.width(10.dp))

            // Go to 'Route' Button
            if (uiState.route != null) {
                OutlinedButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        navController.navigate(NavRoutes.HOME_SCREEN)
                    }
                ) {
                    Text("Route")
                    Icon(Icons.Filled.Route, contentDescription = "")
                }
            }
        }

    }

}

@Composable
private fun LocationInputSection(
    mainNavController: NavController,
    startLocation: String,
    destination: String,
    onStartChange: (String) -> Unit,
    onDestChange: (String) -> Unit,
    onSwap: () -> Unit,
    isStartLocationClicked: () -> Unit,
    isDestinationClicked: () -> Unit,
) {

    var isSwapClicked by remember { mutableStateOf(false) }
    val rotateAnimation by animateFloatAsState(
        label = "",
        targetValue = if (isSwapClicked) 180f else 0f,
        finishedListener = { isSwapClicked = false }
    )

    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(Modifier.padding(16.dp)) {

            // Start Location
            OutlinedInputField(
                modifier = Modifier.clickable { isStartLocationClicked() },
                value = startLocation,
                onValueChange = onStartChange,
                label = R.string.start_location,
                leadingIcon = Icons.Default.LocationOn,
                readOnly = true,
                enabled = false
            )


            // Swap Button
            SwapButton(onSwap, modifier = Modifier.align(Alignment.CenterHorizontally))

            // Destination
            OutlinedInputField(
                modifier = Modifier.clickable { isDestinationClicked() },
                value = destination,
                onValueChange = onDestChange,
                label = R.string.destination,
                leadingIcon = Icons.Default.Flag,
                readOnly = true,
                enabled = false
            )

        }
    }
}

@Composable
private fun RouteResultsSection(route: EvRoute) {
    // Route Summary
    Card(
        modifier = Modifier.clickable { },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF05C93C).copy(alpha = 0.1f)
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Route Summary",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF05C93C)
            )

            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Directions, "Distance", tint = Color(0xFF05C93C))
                    Text(route.distance, style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AccessTime, "Duration", tint = Color(0xFF05C93C))
                    Text(route.duration, style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.BatteryChargingFull, "Energy", tint = Color(0xFF05C93C))
                    Text(route.energyRequired, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // Charging Stops
    Card(
        modifier = Modifier.clickable {},
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "Charging Stops",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF05C93C)
            )

            Spacer(Modifier.height(8.dp))

            route.chargingStops.forEach { stop ->
                ChargingStopItem(stop = stop)
                if (stop != route.chargingStops.last()) {
                    Divider(Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun ChargingStopItem(stop: ChargingStop) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            Icons.Default.EvStation,
            "Charger",
            tint = Color(0xFF05C93C),
            modifier = Modifier.size(40.dp)
        )

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            Text(stop.stationName, fontWeight = FontWeight.Bold)
            Text("Arrival: ~${stop.estimatedBatteryLevel}% battery")
            Text("${stop.distanceFromPrevious} • ${stop.chargersAvailable} chargers")
        }

        Text(stop.chargingTime, color = Color(0xFF05C93C))
    }
}

@Composable
fun SwapButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary,
) {
    // Animation states
    var isAnimating by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    // Main rotation animation
    val animatedRotation by animateFloatAsState(
        targetValue = if (rotationAngle == 0f) 0f else rotationAngle * 180,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { isAnimating = false },
        label = "rotation"
    )

    // Scale animation for press effect
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 0.85f else 1f,
        animationSpec = tween(
            durationMillis = if (isAnimating) 100 else 200,
            easing = if (isAnimating) FastOutSlowInEasing else LinearOutSlowInEasing
        ),
        label = "scale"
    )

    // Background color animation
    val backgroundColor by animateColorAsState(
        targetValue = if (isAnimating)
            tint.copy(alpha = 0.1f)
        else
            MaterialTheme.colorScheme.surface,
        animationSpec = tween(300),
        label = "backgroundColor"
    )

    IconButton(
        onClick = {
            rotationAngle += 1f
            onClick()
        },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Rounded.SwapVert,
            contentDescription = "Swap fields",
            tint = tint,
            modifier = Modifier
                .size(28.dp)
                .graphicsLayer {
                    rotationX = animatedRotation
                }
        )
    }
}
