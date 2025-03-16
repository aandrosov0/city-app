package aandrosov.city.app.ui.states

data class LoginScreenState(
    val cities: List<CityState> = listOf(),
    val selectedCity: CityState? = null,
    val isLoading: Boolean = false,
)


