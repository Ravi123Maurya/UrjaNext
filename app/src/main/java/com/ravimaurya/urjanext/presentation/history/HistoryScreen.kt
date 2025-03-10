package com.ravimaurya.urjanext.presentation.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("History")
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