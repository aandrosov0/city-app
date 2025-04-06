package aandrosov.city.app.ui.states

import aandrosov.city.data.models.WeatherModel

data class WeatherState(
    val currentTemperature: Double = 0.0,
    val currentWeatherCode: WeatherCodeState = WeatherCodeState.THUNDERSTORM
)

fun WeatherModel.toState() = WeatherState(
    currentTemperature = current.temperature,
    currentWeatherCode = current.weatherCode.toState()
)