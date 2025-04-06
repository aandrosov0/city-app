package aandrosov.city.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherCurrentModel(
    @SerialName("temperature_2m") val temperature: Double,
    @SerialName("weather_code") val weatherCode: WeatherCodeModel
)
