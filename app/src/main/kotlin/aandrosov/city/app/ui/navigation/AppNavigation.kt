package aandrosov.city.app.ui.navigation

import aandrosov.city.app.ui.screens.ArticleScreen
import aandrosov.city.app.ui.screens.HomeScreen
import aandrosov.city.app.ui.screens.LoginScreen
import aandrosov.city.app.ui.screens.OnboardingScreen
import aandrosov.city.app.ui.screens.SplashScreen
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

@Serializable
data class Article(val id: Long)

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Login,
        modifier = modifier
    ) {
        composable<Splash> { SplashScreen(navController::navigateInApp) }
        composable<Onboarding> { OnboardingScreen(navController::navigateInApp) }
        composable<Login> { LoginScreen(navController::navigateInApp) }
        composable<Home> { HomeScreen(rootNavController = navController) }
        composable<Article> { ArticleScreen(navController::navigateUp) }
    }
}

private fun NavController.navigateInApp(route: Any) {
    popBackStack()
    navigate(route)
}