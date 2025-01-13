// ... other imports ...
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination

import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ravimaurya.urjanext.presentation.navigation.NavRoutes

object ConstantItems {
    val BottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = NavRoutes.HOME_SCREEN
        ),
        BottomNavItem(
            label = "Station",
            icon = Icons.Filled.Settings,
            route = NavRoutes.STATION_SCREEN
        ),
        BottomNavItem(
            label = "Scanner",
            icon = Icons.Filled.QrCodeScanner,
            route = NavRoutes.SCANQR_SCREEN
        ),
        BottomNavItem(
            label = "History",
            icon = Icons.Filled.List,
            route = NavRoutes.HISTORY_SCREEN
        ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Filled.Person,
            route = NavRoutes.PROFILE_SCREEN
        )
    )
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String,
)
@Composable
fun BottomUrjaBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarStrokeColor = MaterialTheme.colorScheme.primary

    BottomAppBar(
        modifier = Modifier
            .drawBehind {
                drawLine(
                    color = bottomBarStrokeColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 6f,
                    cap = StrokeCap.Round
                )
            }
    ) {
        ConstantItems.BottomNavItems.forEach { bottomItem ->
            NavigationBarItem(
                label = { Text(bottomItem.label) },
                selected = currentRoute == bottomItem.route,
                icon = { Icon(bottomItem.icon, contentDescription = null) },
                onClick = {

                        navController.navigate(bottomItem.route) {
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            // Restore the state of the selected item
                            restoreState = true
                            // Avoid multiple copies of the same destination when re-selecting the same item
                            launchSingleTop = true

                        }

                },
                alwaysShowLabel = true
            )
        }
    }
}
