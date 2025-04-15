package com.ravimaurya.urjanext.presentation.history

import androidx.appcompat.widget.SearchView
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.ravimaurya.urjanext.util.SortHistory
import com.ravimaurya.urjanext.util.SortOptions
import com.ravimaurya.urjanext.util.sortBy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, mainNavController: NavController) {


    var isSortByClicked by remember { mutableStateOf(false) }
    var sort by remember { mutableStateOf("") }
    var newHistoryList by remember { mutableStateOf(dummyHistoryList) }
    if (isSortByClicked) {
        SortByBottomSheet(sort, onDismissSortBySheet = {
            isSortByClicked = false
            sort = ""
        }) { sortBy ->
            // On Apply Click
            newHistoryList = SortHistory(sortBy).sort(newHistoryList)
            isSortByClicked = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = .5f))
            .padding(12.dp),
    ) {

        // Sort History
        SortHistory(sortBy) {
            isSortByClicked = true
            sort = it
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(newHistoryList.size) { index ->
                key(index) {

                    // History Details
                    HistoryDetails(
                        stationName = newHistoryList[index].stationName,
                        location = newHistoryList[index].stationLocation,
                        date = newHistoryList[index].chargingDate,
                        amountPaid = newHistoryList[index].amountPaid,
                        energyConsumed = newHistoryList[index].energyConsumed,
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
fun SortHistory(
    sortBy: List<String>,
    isSortByClicked: (String) -> Unit,
) {

    LazyRow() {
        items(sortBy.size) { index ->
            key(index) {
                SortBy(sortBy[index]) {  isSortByClicked(it) }
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
        Color.Yellow,
        Color.Cyan,
        Color.Magenta,
        Color.Blue,
        Color.Red
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
    isSortByClicked: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { isSortByClicked(sortBy) }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = sortBy, fontSize = 14.sp)
        Icon(Icons.Filled.ArrowDropDown, "")
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
                    ) { Icon(Icons.Filled.Check, contentDescription = "", tint = Color.White) }
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
                    fontWeight = FontWeight.Bold
                )
            }

            // Location
            Text(text = "Nagpur-Mumbai Expressway (Samruddhi Mahamarg), Nagpur")

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortByBottomSheet(
    sortWith: String,
    onDismissSortBySheet: () -> Unit,
    onApplyClick: (SortOptions) -> Unit
) {

    var selectedOption by remember { mutableStateOf(SortOptions.NULL) }
    val amountOptions = listOf(SortOptions.HIGHEST, SortOptions.LOWEST)
    val dateOptions = listOf(SortOptions.NEWEST, SortOptions.OLDEST)

    ModalBottomSheet(
        onDismissRequest = {
            onDismissSortBySheet()
        },
    ) {

        Column {

            when (sortWith) {
                sortBy[0] -> {
                    dateOptions.forEach {
                        SortOption(it.name, selectedOption == it) { selectedOption = it }
                    }
                }
                sortBy[1] -> {
                    amountOptions.forEach {
                        SortOption(it.name, selectedOption == it) { selectedOption = it }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray
                    ),
                    onClick = { onDismissSortBySheet() }
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = { onApplyClick(selectedOption) }
                ) {
                    Text("Apply")
                }
            }

        }

    }
}

@Composable
fun SortOption(option:String, selected: Boolean, onOptionClick: () -> Unit){
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected, onClick = { onOptionClick() })
        Text(option)
    }
}
