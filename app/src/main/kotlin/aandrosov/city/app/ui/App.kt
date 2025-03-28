package aandrosov.city.app.ui

import aandrosov.city.app.ui.navigation.AppNavigation
import aandrosov.city.app.ui.themes.AppTheme
import aandrosov.city.data.dataModule
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinApplication

@Composable
fun App() = KoinApplication({ modules(dataModule, appModule) }) {
    AppTheme { AppNavigation(rememberNavController()) }
}