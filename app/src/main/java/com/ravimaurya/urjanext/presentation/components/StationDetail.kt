package com.ravimaurya.urjanext.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.outlined.EvStation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravimaurya.urjanext.R

@Preview(showBackground = true)
@Composable
fun StationDetail23(modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp)
            .clip(RoundedCornerShape(5.dp))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Station Icon - Open
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(imageVector = Icons.Outlined.EvStation, "")
            Text("Open", color = Color.Green)
        }

        // Time
        Text("09.00-24.00", color = Color.LightGray, textAlign = TextAlign.End)

        // Connector type
        Column(
            modifier = Modifier
                .padding(start = 40.dp),
        ) {
            Text("Connector type", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Row(

                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.Cyan)
                )
                Text("CCS5-2")
                Spacer(Modifier.width(50.dp))
                Box(
                    Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                )
                Text("BHA-98")
            }
        }

        // Continue Button
        BigButton(
            label = R.string.continue_,
            onClick = {

            }
        )
    }


}

@Composable
fun ChargingStationCard(
    isClicked: Boolean,
    modifier: Modifier = Modifier,
    isOpen: Boolean = true,
    hours: String = "09.00 - 24.00",
    selectedConnector: String = "CCS5-2",
    connectorOptions: List<String> = listOf("CCS5-2", "BHA-98"),
    onConnectorSelected: (String) -> Unit = {},
    onContinueClick: () -> Unit = {},
) {

    AnimatedVisibility(
        visible = isClicked,
        enter = slideInVertically(initialOffsetY = { it }),  // Slide in from bottom
        exit = if (isClicked) fadeOut() else slideOutVertically(targetOffsetY = { it })    // Slide out to bottom
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(5.dp), clip = false),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Header with station icon and status
                StationHeader(isOpen = isOpen, hours = hours)

                // Connector type section
                Text(
                    text = "Connector type",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp)
                )

                // Connector options
                ConnectorOptions(
                    options = connectorOptions,
                    selectedOption = selectedConnector,
                    onOptionSelected = onConnectorSelected
                )

                // Continue button
                BigButton(
                    label = R.string.continue_,
                    onClick = {
                        onContinueClick()
                    }
                )
            }
        }
    }

}

@Composable
private fun StationHeader(isOpen: Boolean, hours: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.EvStation,
            contentDescription = "Charging Station",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(30.dp)
        )
        StationStatus(isOpen = isOpen, hours = hours)
    }
}

@Composable
private fun StationIcon() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .border(2.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(4.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {

    }
}

@Composable
private fun StationStatus(isOpen: Boolean, hours: String) {
    Column(horizontalAlignment = Alignment.End) {
        Text(
            text = if (isOpen) "Open" else "Closed",
            color = if (isOpen) Color(0xFF00C853) else Color.Red,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )

        Text(
            text = hours,
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ConnectorOptions(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        options.forEach { option ->
            val isSelected = option == selectedOption
            ConnectorOption(
                text = option,
                isSelected = isSelected,
                onClick = { onOptionSelected(option) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ConnectorOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
