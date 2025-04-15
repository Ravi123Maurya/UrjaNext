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
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravimaurya.urjanext.domain.model.EVStation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoSearchBar(
    stationPrediction: List<EVStation> = emptyList(),
    onSearchQueryChange: (String) -> Unit = {},
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
        active = active
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(stationPrediction.size) { index ->

                SearchList(
                    title = stationPrediction[index].name,
                    text = stationPrediction[index].address,
                    onLocationClick = {
                        query = stationPrediction[index].name.toString()
                        active = false
                    }
                )

                HorizontalDivider(Modifier.fillMaxSize(.6f))
            }
        }
    }
}

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
            .background(MaterialTheme.colorScheme.onPrimary)
            .clickable { onLocationClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(Icons.Outlined.LocationOn, "")

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
            modifier = Modifier
                .rotate(-90f),
            imageVector = Icons.Filled.ArrowOutward, contentDescription = "")

    }
}


