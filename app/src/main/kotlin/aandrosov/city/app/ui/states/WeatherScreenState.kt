package aandrosov.city.app.ui.states

data class WeatherScreenState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val weather: WeatherState = WeatherState()
)
