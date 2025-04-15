package com.ravimaurya.urjanext.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ravimaurya.urjanext.presentation.home.HomeScreen
import com.ravimaurya.urjanext.presentation.welcome.WelcomeScreen
import com.ravimaurya.urjanext.presentation.auth.AuthenticationScreen
import com.ravimaurya.urjanext.presentation.history.HistoryDetailScreen
import com.ravimaurya.urjanext.presentation.history.HistoryScreen
import com.ravimaurya.urjanext.presentation.mainscreens.MainScreens
import com.ravimaurya.urjanext.presentation.profile.ProfileScreen
import com.ravimaurya.urjanext.presentation.scanner.ScannerScreen
import com.ravimaurya.urjanext.presentation.splashscreen.SplashScreen
import com.ravimaurya.urjanext.presentation.station.EVMapScreen
import com.ravimaurya.urjanext.presentation.station.StationScreen


object NavRoutes {
    const val SPLASH_SCREEN = "splash_screen"
    const val WELCOME_SCREEN = "welcome"
    const val AUTHENTICATION_SCREEN = "authentication"

    const val HOME_SCREEN = "home"
    const val STATION_SCREEN = "station"
    const val SCANQR_SCREEN = "scanqr"
    const val HISTORY_SCREEN = "history"
    const val HISTORY_DETAIL_SCREEN = "history-details"
    const val PROFILE_SCREEN = "profile"

    const val HOME_NAV_GRAPH = "homeNavGraph"
}
@Composable
fun MainNavGraph() {

    val mainNavController = rememberNavController()

    NavHost(
        navController = mainNavController,
        startDestination = NavRoutes.SPLASH_SCREEN
    ) {
        // Splash
        composable(NavRoutes.SPLASH_SCREEN) { SplashScreen(mainNavController) }
        // Welcome
        composable(NavRoutes.WELCOME_SCREEN) { WelcomeScreen(mainNavController) }
        // Authentication
        composable(NavRoutes.AUTHENTICATION_SCREEN) { AuthenticationScreen(mainNavController) }
        // History Details
        composable(NavRoutes.HISTORY_DETAIL_SCREEN) { HistoryDetailScreen(mainNavController) }

        // Home Nav Graph (Wrapper for Main Screens with Bottom Navigation Bar)
         composable(NavRoutes.HOME_NAV_GRAPH) { MainScreens(mainNavController) }
    }
}

@Composable
fun HomeScreensGraph(
    navController: NavHostController,
    mainNavController: NavController,
    isFabClicked: Boolean,
    isSearchClicked: Boolean
){

    NavHost(
        navController = navController,
        startDestination = NavRoutes.STATION_SCREEN,
    ){

        composable(NavRoutes.HOME_SCREEN) {
            HomeScreen(navController, isFabClicked = isFabClicked, isSearchClicked = isSearchClicked)
        }
        composable(NavRoutes.STATION_SCREEN) {
            StationScreen(navController)
        }
        composable(NavRoutes.SCANQR_SCREEN) {
            ScannerScreen(navController)
        }
        composable(NavRoutes.HISTORY_SCREEN) {
            HistoryScreen(navController, mainNavController)
        }
        composable(NavRoutes.PROFILE_SCREEN) {
            ProfileScreen(navController, mainNavController)
        }
    }

}