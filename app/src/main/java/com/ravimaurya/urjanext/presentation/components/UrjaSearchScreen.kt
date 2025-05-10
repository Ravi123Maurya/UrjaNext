package com.ravimaurya.urjanext.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.LatLng
import com.ravimaurya.urjanext.domain.model.EVStation
import com.ravimaurya.urjanext.presentation.home.EVSearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrjaSearchScreen(
    isVisible: Boolean = false,
    onDismiss: () -> Unit = {},
    onSearchQueryChanged: (String) -> Unit = {},
    placeHolder: String = "Search here",
    onLocationSelected: (EVStation) -> Unit = { },
    evSearchViewModel: EVSearchViewModel = hiltViewModel()
    ) {

    var query by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(false) }
    var stationPrediction by remember { mutableStateOf(emptyList<EVStation>()) }
    var searchJob by remember { mutableStateOf<Job?>(null) }
//    val focusRequester = remember { FocusRequester() }

//    LaunchedEffect(Unit) {
//        focusRequester.requestFocus()
//    }

    LaunchedEffect(query) {
        searchJob?.cancel()
        searchJob = launch {
            delay(300)
            stationPrediction = emptyList()
            evSearchViewModel.searchEVStations(query) { stations ->
                stationPrediction =
                    stationPrediction + stations.filter { it !in stationPrediction } // Replace the existing list with fresh results
            }
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)) +
                slideInVertically(animationSpec = tween(300)) { it / 2 },
        exit = fadeOut(animationSpec = tween(300)) +
                slideOutVertically(animationSpec = tween(300)) { it / 2 }
    ) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        modifier = Modifier.padding(10.dp),
                        title = {},
                        navigationIcon = {

                            TextField(
                                modifier = Modifier
                                    .fillMaxWidth(),
//                                    .focusRequester(focusRequester),
                                value = query,
                                onValueChange = {
                                    query = it
                                    onSearchQueryChanged(it)
                                },
                                placeholder = { Text("Search Location ") },
                                leadingIcon = {
                                    // Nav Back
                                    IconButton(
                                        onClick = onDismiss
                                    ) {
                                        Icon(Icons.Filled.ArrowBack, "Go Back")
                                    }
                                },
                                trailingIcon = {
                                    if (query.isNotEmpty()) {
                                        // Clear Query
                                        IconButton(
                                            onClick = { query = "" }
                                        ) {
                                            Icon(Icons.Filled.Clear, "Clear")
                                        }
                                    }

                                },
                                shape = ShapeDefaults.ExtraLarge,
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                )
                            )

                        }
                    )
                }
            ) { innerPadding ->

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    horizontalAlignment = Alignment.End
                ) {
                    items(stationPrediction.size) { index ->

                        SearchList(
                            title = stationPrediction[index].name,
                            text = stationPrediction[index].address,
                            onLocationClick = {
                                query = stationPrediction[index].name.toString()
                                onLocationSelected(
                                    EVStation(
                                        name = stationPrediction[index].name,
                                        address = stationPrediction[index].address,
                                        location = stationPrediction[index].location,
                                        availableChargers = 1
                                    )
                                )
                            }
                        )

                        HorizontalDivider()
                    }
                }

            }
        }
    }



}

