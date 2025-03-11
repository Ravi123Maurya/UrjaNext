package com.ravimaurya.urjanext.presentation.history

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ravimaurya.urjanext.domain.model.dummyHistoryList
import com.ravimaurya.urjanext.presentation.components.NavBackScaffold
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, mainNavController: NavController) {

    var isDetailClicked by remember { mutableStateOf(false) }
    var isSortByClicked by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = .5f))
            .padding(12.dp),
    ) {

        SortHistory()
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(dummyHistoryList.size) { index ->
                key(index) {
                    HistoryDetails(
                        stationName = dummyHistoryList[index].stationName,
                        location = dummyHistoryList[index].stationLocation,
                        date = dummyHistoryList[index].chargingDate,
                        amountPaid = dummyHistoryList[index].amountPaid,
                        energyConsumed = dummyHistoryList[index].energyConsumed,
                        onHistoryDetailClick = {
                            mainNavController.navigate(NavRoutes.HISTORY_DETAIL_SCREEN)
                        }
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }

}

@Composable
fun SortHistory() {

    val sortBy = listOf("Date", "Amount", "Payment Method", "Status")

    LazyRow() {
        items(sortBy.size) { index ->
            key(index) {
                SortBy(sortBy[index])
                Spacer(Modifier.width(15.dp))
            }

        }
    }

}

@Composable
fun HistoryDetails(
    stationName: String,
    location: String,
    date: String,
    amountPaid: Double,
    energyConsumed: Double,
    onHistoryDetailClick: () -> Unit,
) {

    val iconColorList = listOf(
        Color.Yellow.copy(green = .5f),
        Color.Cyan,
        Color.Magenta,
        Color.Blue.copy(red = .6f, green = .4f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable { onHistoryDetailClick() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(1.dp, Color.Gray, CircleShape)
                    .background(
                        iconColorList
                            .random()
                            .copy(alpha = .5f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "${stationName[0]}", fontSize = 25.sp)
            }

            // Station Name | Location | Date
            Column {
                Text(
                    text = if (stationName.length > 15) "${
                        stationName.substring(
                            0,
                            13
                        )
                    }..." else stationName,
                    fontSize = 18.sp,
                )
                Text(
                    text = if (location.length > 21) "${
                        location.substring(
                            0,
                            17
                        )
                    }..." else location, fontSize = 14.sp, color = Color.Gray
                )
                Text(text = date, fontSize = 14.sp, color = Color.LightGray)
            }
        }


        // Amount Paid | Energy Consumed
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$amountPaid₹", fontSize = 20.sp)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$energyConsumed kWh", fontSize = 16.sp, color = Color.Gray)
                Text("Energy consumed", fontSize = 12.sp, color = Color.LightGray)
            }
        }
    }

}

@Composable
fun SortBy(
    sortBy: String,
    isSortByClicked: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { isSortByClicked() }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = sortBy, fontSize = 14.sp)
        Icon(Icons.Filled.ArrowDropDown, "")
    }
}

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrjaSearchField(
    query: (String) -> Unit = {},
) {

    var isActive by remember { mutableStateOf(false) }
    var myQuery by remember { mutableStateOf("") }


    DockedSearchBar(
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
        shape = ShapeDefaults.Medium
    ) {
        Text("Search Content")
    }


}


@Composable
fun HistoryDetailScreen(navController: NavController) {
    NavBackScaffold(
        navController = navController
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
                    .background(Color.Yellow),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "S", fontSize = 40.sp, fontWeight = FontWeight.Bold)
            }

            // Station Name
            Text(text = "Urja Power House", fontSize = 16.sp, fontWeight = FontWeight.Bold)

            // Amount Paid | Payment Status
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "342₹", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.Green),
                        contentAlignment = Alignment.Center
                    ){ Icon(Icons.Filled.Check, contentDescription = "", tint = Color.White) }
                    Text(text = "Completed", fontSize = 12.sp)
                }
            }

            // Energy Consumed
            Column {
                Text(text = "24 kWh", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Green)
                        .padding(horizontal = 5.dp),
                    text = "Energy consumed",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold)
            }

            // Location
            Text(text = "Nagpur-Mumbai Expressway (Samruddhi Mahamarg), Nagpur")

        }
    }
}