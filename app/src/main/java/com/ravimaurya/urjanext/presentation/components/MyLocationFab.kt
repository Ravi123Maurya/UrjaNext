package com.ravimaurya.urjanext.presentation.components

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview(showBackground = true)
@Composable
fun MyLocationFab(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    hasLocationPermission: Boolean = true
) {

    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(16.dp)
    ){
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Green.copy(alpha = .7f))
                .clickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ){
            Icon(
                imageVector = if(hasLocationPermission) Icons.Filled.MyLocation else Icons.Filled.LocationDisabled,
                contentDescription = "",
                tint = if (hasLocationPermission) Color.Black else Color.Red
            )
        }
    }

}