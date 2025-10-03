package com.ravimaurya.urjanext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.MapsInitializer
import com.ravimaurya.urjanext.theme.UrjaNextTheme
import com.ravimaurya.urjanext.presentation.navigation.MainNavGraph
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        MapsInitializer.initialize(applicationContext)

        setContent {
            UrjaNextTheme {


                MainNavGraph()

            }
        }
    }
}



