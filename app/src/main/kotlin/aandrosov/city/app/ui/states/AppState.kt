package aandrosov.city.app.ui.states

data class AppState(
    val isAuthorized: Boolean = false,
    val settings: AppSettingsState = AppSettingsState()
)
