package com.ravimaurya.urjanext.presentation.mainscreens

import BottomUrjaBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.history.HistoryScreen
import com.ravimaurya.urjanext.presentation.history.UrjaSearchField
import com.ravimaurya.urjanext.presentation.home.HomeScreen
import com.ravimaurya.urjanext.presentation.navigation.HomeScreensGraph
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import com.ravimaurya.urjanext.presentation.profile.ProfileScreen
import com.ravimaurya.urjanext.presentation.scanner.ScannerScreen
import com.ravimaurya.urjanext.presentation.station.StationScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreens(authNavController: NavHostController) {
    val strokeColor = MaterialTheme.colorScheme.primary

    val homeNavController = rememberNavController()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var title by remember { mutableStateOf("") }
    var hasSearchClicked by remember { mutableStateOf(false) }
    var isFabClicked by remember { mutableStateOf(false) }

    val searchIconAnimation by animateFloatAsState(
        label = "",
        targetValue = if (hasSearchClicked) -100f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier,
//                    .drawBehind {
//                        drawLine(
//                            color = strokeColor,
//                            start = Offset(0f, size.height),
//                            end = Offset(size.width, size.height),
//                            strokeWidth = 6f
//                        )
//                    },
                title = {
                    when (currentRoute) {
                        NavRoutes.HOME_SCREEN -> title = "UrjaNext"
                        NavRoutes.STATION_SCREEN -> title = stringResource(R.string.station)
                        NavRoutes.SCANQR_SCREEN -> title = stringResource(R.string.scan)
                        NavRoutes.HISTORY_SCREEN -> title = stringResource(R.string.history)
                        NavRoutes.PROFILE_SCREEN -> title = stringResource(R.string.profile)
                    }
                    Text(title)
                },
                navigationIcon = {

                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(10.dp),
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary
                    )


                },
                actions = {
                    IconButton(onClick = {
                        // Search Action
                        hasSearchClicked = !hasSearchClicked
                    }) {
                        Icon(
                            Icons.Filled.Search, "",
                            modifier = Modifier
                                .rotate(searchIconAnimation)
                        )
                    }
                    IconButton(onClick = {
                        // Urja Notification
                    }) {
                        Icon(Icons.Filled.NotificationsNone, "")
                    }
                }
            )
        },
        bottomBar = {
            BottomUrjaBar(homeNavController)
        },
        floatingActionButton = {
            AnimatedVisibility(currentRoute == NavRoutes.HOME_SCREEN) {
                UrjaFab(fabClick = { isFabClicked = !isFabClicked }, isFabClicked)
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .zIndex(1f)
            ) {


                AnimatedVisibility(hasSearchClicked) {
                    UrjaSearchField() { }
                }

            }

            HomeScreensGraph(homeNavController, authNavController, isFabClicked)
        }
    }
}


@Composable
fun UrjaFab(
    fabClick: () -> Unit,
    isFabClicked: Boolean,
) {
    FloatingActionButton(
        onClick = {
            fabClick()
        },
        shape = CircleShape
    ) {

        Icon(if (isFabClicked) Icons.Filled.Close else Icons.Filled.Map, "")
    }
}



