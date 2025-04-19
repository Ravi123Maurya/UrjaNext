package com.ravimaurya.urjanext.presentation.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HomeMax
import androidx.compose.material.icons.filled.LocationDisabled
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun MyLocationFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    hasLocationPermission: Boolean = true
) {

    var isAnimating by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isAnimating) 360f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        finishedListener = { isAnimating = false }
    )

    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(16.dp)
    ){
        Box(
            modifier = modifier
                .size(45.dp)
                .shadow(elevation = 6.dp, shape = CircleShape, spotColor = Color.Black.copy(alpha = 0.3f))
                .clip(CircleShape)
                .background(
                   Color.Green.copy(alpha = .8f)
                )
                .clickable {
                    isAnimating = true
                    onClick()
                }
                .graphicsLayer {
                    rotationZ = rotation
                },
            contentAlignment = Alignment.Center
        ) {
            // Add subtle pulse animation when location is available
            val infiniteTransition = rememberInfiniteTransition(label = "pulse")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = if (hasLocationPermission) 1.1f else 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulse scale"
            )

            Icon(
                imageVector = if (hasLocationPermission) Icons.Filled.MyLocation else Icons.Filled.LocationDisabled,
                contentDescription = if (hasLocationPermission) "My Location" else "Location Disabled",
                tint = if (hasLocationPermission) Color.Black else Color.Red,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = if (hasLocationPermission) scale else 1f
                        scaleY = if (hasLocationPermission) scale else 1f
                    }
                    .size(24.dp)
            )
        }
    }

}