package aandrosov.city.app.ui.states

data class LoginScreenState(
    val current: CityState = CityState(),
    val cities: List<CityState> = listOf(),
    val isError: Boolean = false,
)


