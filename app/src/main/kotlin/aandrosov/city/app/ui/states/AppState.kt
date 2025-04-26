package aandrosov.city.app.ui.states

data class AppState(
    val isLoading: Boolean = false,
    val isAuthorized: Boolean = false,
    val isDarkModeEnabled: Boolean = false,
)
