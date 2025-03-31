package aandrosov.city.app.ui.navigation

import aandrosov.city.app.ui.screens.EventsScreen
import aandrosov.city.app.ui.screens.MenuScreen
import aandrosov.city.app.ui.screens.NewsScreen
import aandrosov.city.app.ui.screens.TicketsScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object News

@Serializable
data object Tickets

@Serializable
data object Events

@Serializable
data object Menu

@Composable
fun HomeNavigation(
    navController: NavHostController,
    onRootNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = News,
        modifier = modifier
    ) {
        composable<News> { NewsScreen(onRootNavigate) }
        composable<Tickets> { TicketsScreen() }
        composable<Events> { EventsScreen() }
        composable<Menu> { MenuScreen() }
    }
}