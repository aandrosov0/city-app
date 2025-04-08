package aandrosov.city.app.ui.states

data class MenuScreenState(
    val currentCity: CityState = CityState(),
    val cities: List<CityState> = emptyList(),
    val isDarkModeEnabled: Boolean = false,
    val isError: Boolean = false,
)
