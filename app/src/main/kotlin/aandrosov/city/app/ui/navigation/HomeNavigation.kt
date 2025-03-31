package aandrosov.city.app.ui.navigation

import aandrosov.city.app.ui.screens.EventsScreen
import aandrosov.city.app.ui.screens.NewsScreen
import aandrosov.city.app.ui.screens.TicketsScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    rootNavController: NavHostController,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = News,
        modifier = modifier
    ) {
        composable<News> { NewsScreen(rootNavController::navigate) }
        composable<Tickets> { TicketsScreen() }
        composable<Events> { EventsScreen() }
        composable<Menu> {  }
    }
}