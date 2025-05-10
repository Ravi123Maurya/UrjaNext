package com.ravimaurya.urjanext.presentation.mainscreens

import BottomUrjaBar
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.home.EVSearchViewModel
import com.ravimaurya.urjanext.presentation.navigation.HomeScreensGraph
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import com.ravimaurya.urjanext.theme.Green40
import com.ravimaurya.urjanext.util.yeti
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreens(authNavController: NavHostController) {

    val evSearchViewModel: EVSearchViewModel = hiltViewModel()
    val isSearchClicked by evSearchViewModel.isSearchClicked.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val homeNavController = rememberNavController()
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var title by remember { mutableStateOf("") }
    var isFabClicked by remember { mutableStateOf(false) }

    val searchIconAnimation by animateFloatAsState(
        label = "",
        targetValue = if (isSearchClicked) -100f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
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
                    if(currentRoute == NavRoutes.HOME_SCREEN){
                        IconButton(onClick = {
                            // Search Action
                            evSearchViewModel.searchClickedOrDismiss()
                        }) {
                            Icon(
                                Icons.Filled.Search, "",
                                modifier = Modifier
                                    .rotate(searchIconAnimation)
                            )
                        }
                    }
                    // Notification
                    AnimatedNotificationButton { yeti(context) }

                }
            )
        },
        bottomBar = {
            BottomUrjaBar(homeNavController)
        },
    ) { innerPadding ->

        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {

            HomeScreensGraph(homeNavController, authNavController, isFabClicked, isSearchClicked){ evSearchViewModel.searchClickedOrDismiss()}
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrjaSearchField(
    query: (String) -> Unit = {},
) {

    var isActive by remember { mutableStateOf(false) }
    var myQuery by remember { mutableStateOf("") }


    SearchBar(
        modifier = Modifier,
        query = myQuery,
        onSearch = {},
        onQueryChange = {
            myQuery = it
            query(myQuery)
        },
        onActiveChange = {
            println("Activated")
        },
        active = isActive,
        leadingIcon = { Icon(Icons.Filled.Search, "Search Location") },
        trailingIcon = {
            IconButton(
                onClick = { myQuery = "" }
            ) {
                Icon(Icons.Filled.Clear, "Clear inputs")
            }
        },
        placeholder = { Text("Urja Search here") },
        shape = ShapeDefaults.Medium,
    ) {
        Text("Search EV stations or location")
    }


}



@Composable
fun AnimatedNotificationButton(onClick: () -> Unit) {
    // Animation states
    var isAnimating by remember { mutableStateOf(false) }
    val animatedScale = remember { Animatable(1f) }
    val animatedRotation = remember { Animatable(0f) }
    val animatedColor = remember { Animatable(Color.Gray.copy(alpha = 0f)) }

    // Handle animation when the button is clicked
    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            // First grow and rotate
            launch {
                animatedScale.animateTo(
                    targetValue = 1.3f,
                    animationSpec = tween(150, easing = FastOutSlowInEasing)
                )
                animatedScale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }

            // Rotate back and forth
            launch {
                animatedRotation.animateTo(
                    targetValue = -20f,
                    animationSpec = tween(100)
                )
                animatedRotation.animateTo(
                    targetValue = 20f,
                    animationSpec = tween(100)
                )
                animatedRotation.animateTo(
                    targetValue = -10f,
                    animationSpec = tween(100)
                )
                animatedRotation.animateTo(
                    targetValue = 10f,
                    animationSpec = tween(100)
                )
                animatedRotation.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(100)
                )
            }

            // Ripple effect
            launch {
                animatedColor.animateTo(
                    targetValue = Green40.copy(alpha = 0.3f),
                    animationSpec = tween(100)
                )
                animatedColor.animateTo(
                    targetValue = Color.Gray.copy(alpha = 0f),
                    animationSpec = tween(300)
                )
            }

            isAnimating = false
        }
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        // Background ripple effect
        Box(
            modifier = Modifier
                .size(48.dp)
                .scale(animatedScale.value + 0.2f)
                .background(animatedColor.value, CircleShape)
        )

        // The notification icon button
        IconButton(
            onClick = {
                isAnimating = true
                onClick()
            },
            modifier = Modifier
                .scale(animatedScale.value)
                .graphicsLayer {
                    rotationZ = animatedRotation.value
                }
        ) {
            Icon(
                imageVector = Icons.Filled.NotificationsNone,
                contentDescription = "Notifications",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}




