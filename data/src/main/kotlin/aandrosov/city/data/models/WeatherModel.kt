package aandrosov.city.data.models

import kotlinx.serialization.Serializable

@Serializable
data class WeatherModel(
    val latitude: Double,
    val longitude: Double,
    val current: WeatherCurrentModel
)
