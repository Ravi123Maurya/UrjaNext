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
import com.ravimaurya.urjanext.theme.UrjaNextTheme
import com.ravimaurya.urjanext.presentation.navigation.MainNavGraph
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UrjaNextTheme {


                MainNavGraph()

            }
        }
    }
}




//https://creatie.ai/goto/DzL2Jjnd?page_id=0:0&layer_id=25:3225&file=139207260573249
//https://www.figma.com/design/bsdgLbmycjH9ibWPfLoG1y/sem-5-project?node-id=0-1&t=zhPJQuSmxtcWXwa4-1