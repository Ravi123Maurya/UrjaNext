package com.ravimaurya.urjanext.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.animation.MotionTiming
import com.ravimaurya.urjanext.domain.model.EVStation
import com.ravimaurya.urjanext.theme.Green40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoSearchBar(
    stationPrediction: List<EVStation> = emptyList(),
    onSearchQueryChange: (String) -> Unit = {},
    searchedLocation: (LatLng) -> Unit = {}
) {


    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(true) }



    DockedSearchBar(
        query = query,
        onQueryChange = {
            query = it
            onSearchQueryChange(query)
        },
        onActiveChange = { active = it },
        onSearch = {},
        trailingIcon = {
            IconButton(
                onClick = { query = "" }
            ) {
                Icon(imageVector = Icons.Filled.Clear, contentDescription = "")
            }
        },
        active = active,
        colors = SearchBarDefaults.colors()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.End
        ) {
            items(stationPrediction.size) { index ->

                SearchList(
                    title = stationPrediction[index].name,
                    text = stationPrediction[index].address,
                    onLocationClick = {
                        query = stationPrediction[index].name.toString()
                        searchedLocation(stationPrediction[index].location)
                        active = false
                    }
                )

                HorizontalDivider()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun SearchList(
    title: String? = "Unknown location",
    text: String? = "Unknown location",
    onLocationClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Green40.copy(alpha = .05f))
            .padding(horizontal = 20.dp, vertical = 13.dp)
            .clickable { onLocationClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .background(Green40)
                .padding(5.dp),
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = ""
        )

        Column(
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            Text(title!!, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                text = text!!,
                )
        }

        Icon(
            tint = Green40,
            modifier = Modifier
                .rotate(-90f),
            imageVector = Icons.Filled.ArrowOutward,
            contentDescription = ""
        )

    }
}


