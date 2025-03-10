package com.ravimaurya.urjanext.presentation.profile

import android.graphics.drawable.shapes.Shape
import android.view.RoundedCorner
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.maps.android.heatmaps.Gradient
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.auth.AuthenticationViewModel
import com.ravimaurya.urjanext.presentation.components.AlertDialogUrja
import com.ravimaurya.urjanext.presentation.components.CircularProgressDialog
import com.ravimaurya.urjanext.presentation.components.NavBackTopAppBar
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import kotlin.math.roundToInt
import kotlin.math.truncate


@Composable
fun ProfileScreen(
    homeNavController: NavController,
    authNavController: NavController,
    authenticationViewModel: AuthenticationViewModel = hiltViewModel(),
) {

    var ecoModeEnabled by remember { mutableStateOf(false) }


    ProfileScreenContent(
        ecoModeEnabled = ecoModeEnabled,
        onEcoModeChanged = { ecoModeEnabled = !ecoModeEnabled },
        onLogoutConfirm = {
        authenticationViewModel.logout()
        authNavController.navigate(NavRoutes.WELCOME_SCREEN){
            popUpTo(0){
                inclusive = true
            }
        }
    })

}

@Composable
fun ProfileScreenContent(
    ecoModeEnabled: Boolean,
    onEcoModeChanged: (Boolean) -> Unit,
    onLogoutConfirm: () -> Unit
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = .5f))
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(3) { index ->
            key(index) {
                CarDetail(
                    ecoModeEnabled,
                    onEcoModeChanged = { onEcoModeChanged(it) }
                )
                Spacer(Modifier.height(8.dp))
            }


        }

        item {
            Logout {
                onLogoutConfirm()
            }
        }
    }

}

@Composable
fun CarDetail(
    ecoModeEnabled:Boolean,
    onEcoModeChanged: (Boolean) -> Unit
    ) {



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text("Your car", color = Color.LightGray)
        Text("Sport Luxury Car")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            // Car Image
            Image(
                painter = painterResource(R.drawable.sporty_car),
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        }

        // Battery Charge Bar
        Text("Battery", color = Color.LightGray)
        BatteryChargeBar()

        // Estimated Range | Car Eco
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("ESTIMATED RANGE", color = Color.LightGray)
                Text("295 km")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("CAR ECO MODE", color = Color.LightGray)
                EcoModeSwitch(checked = ecoModeEnabled, onEcoModeChanged = { onEcoModeChanged(it) })
            }
        }

    }
}


@Composable
fun BatteryChargeBar() {

    val chargeColor = Brush.linearGradient(
        listOf(Color.Red.copy(255f, 10f, 10f), Color.Green),
        tileMode = TileMode.Mirror
    )
    var chargeValue by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        chargeValue = 0.7f
    }

    val batteryCharged by animateFloatAsState(
        label = "charge",
        targetValue = chargeValue,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Charge Bar
        Box(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .height(20.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = .5f)),

            ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(batteryCharged)
                    .height(20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(chargeColor),

                )
        }

        // Charge Text in %
        Text("76%")
    }


}

@Composable
fun EcoModeSwitch(
    checked: Boolean,
    onEcoModeChanged: (Boolean) -> Unit,
    width: Dp = 45.dp,  // Default switch width
    height: Dp = 20.dp  // Default switch height
) {

    // Custom Switch implementation
    val trackColor = if (checked)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceVariant

    val thumbColor = MaterialTheme.colorScheme.onPrimary
    val transition = updateTransition(checked, label = "Switch")
    val thumbPosition by transition.animateDp(
        label = "Thumb position",
        transitionSpec = { spring(stiffness = Spring.StiffnessMedium) }
    ) { isChecked ->
        if (isChecked) width - height + 0.5.dp else 1.dp
    }

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .clip(CircleShape)
            .background(trackColor)
            .clickable { onEcoModeChanged(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(height - 1.dp)
                .offset(x = thumbPosition)
                .padding(2.dp)
                .clip(CircleShape)
                .background(thumbColor)
        )
    }
}

@Composable
fun Logout(
    onLogoutConfirm: () -> Unit,
) {
    var isLogoutClicked by remember {
        mutableStateOf(false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    CircularProgressDialog(isLoading)

    if (isLogoutClicked) {
        AlertDialogUrja(
            title = "Logout",
            text = "Are you sure? Want to logout?",
            onConfirmClick = {
                isLoading = true
                onLogoutConfirm()
            },
            onDismissClick = { isLogoutClicked = false }
        )
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {
                isLogoutClicked = true
            }
        ) {
            Text("Logout")
        }
    }
}