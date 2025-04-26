package aandrosov.city.app.ui

import aandrosov.city.app.ui.navigation.AppNavigation
import aandrosov.city.app.ui.navigation.Home
import aandrosov.city.app.ui.navigation.Onboarding
import aandrosov.city.app.ui.themes.AppTheme
import aandrosov.city.app.ui.viewModels.AppViewModel
import aandrosov.city.data.dataModule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.request.crossfade
import org.koin.android.ext.koin.androidContext
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject

@Composable
fun App() {
    val context = LocalContext.current

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }

    KoinApplication({
        androidContext(context)
        modules(dataModule, appModule)
    }) {
        val appViewModel = koinInject<AppViewModel>()
        val uiState by appViewModel.uiState.collectAsState()
        val navController = rememberNavController()

        val isAuthorized = uiState.isAuthorized
        LaunchedEffect(isAuthorized) {
            if (isAuthorized) {
                navController.navigateInApp(Home)
            } else {
                navController.navigateInApp(Onboarding)
            }
        }

        AppTheme(isDarkModeEnabled = uiState.isDarkModeEnabled) {
            AppNavigation(navController)
        }
    }
}

fun NavController.navigateInApp(route: Any) {
    popBackStack()
    navigate(route) {
        popUpTo(route) { inclusive = true }
    }
}