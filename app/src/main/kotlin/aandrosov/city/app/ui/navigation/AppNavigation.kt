package aandrosov.city.app.ui.navigation

import aandrosov.city.app.ui.screens.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
data object Splash

@Serializable
data object Onboarding

@Serializable
data object Login

@Serializable
data object Home

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Home,
        modifier = modifier
    ) {
        composable<Splash> { SplashScreen(navController::navigateInApp) }
        composable<Onboarding> { OnboardingScreen(navController::navigateInApp) }
        composable<Login> { LoginScreen(navController::navigateInApp) }
        composable<Home> { HomeScreen() }
    }
}

private fun NavController.navigateInApp(route: Any) {
    popBackStack()
    navigate(route)
}