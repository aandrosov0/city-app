package aandrosov.city.app.ui.states

data class LoginScreenState(
    val cities: List<CityState> = listOf(),
    val isLoading: Boolean = false,
)


