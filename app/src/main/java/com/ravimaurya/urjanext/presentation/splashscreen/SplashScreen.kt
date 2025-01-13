package com.ravimaurya.urjanext.presentation.splashscreen

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.ravimaurya.urjanext.R
import com.ravimaurya.urjanext.presentation.auth.AuthenticationViewModel
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authenticationViewModel: AuthenticationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    // val checkUserExist = authenticationViewModel.checkUserExistState.collectAsStateWithLifecycle() // You're not using this state here

    LaunchedEffect(Unit) {
        delay(800)
        try {
            if (auth.currentUser != null) {
                navController.navigate(NavRoutes.HOME_NAV_GRAPH) {
                    popUpTo(NavRoutes.SPLASH_SCREEN) { inclusive = true }
                }
            } else {
                navController.navigate(NavRoutes.WELCOME_SCREEN) {
                    popUpTo(NavRoutes.SPLASH_SCREEN) { inclusive = true }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Something went wrong! Check your internet connection!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Create an infinite transition
    val infiniteTransition = rememberInfiniteTransition("splash_logo_rotation_animation")

    // Define an animated value for rotation
    val rotation by infiniteTransition.animateFloat(
        label = "logo rotation",
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(80.dp)
                .rotate(210f)
                .graphicsLayer(rotationZ = rotation),
            painter = painterResource(R.drawable.logo2),
            contentDescription = "logo"
        )
    }
}