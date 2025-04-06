package aandrosov.city.data.repositories

import aandrosov.city.data.models.WeatherModel

interface WeatherRepository {
    suspend fun getForecast(latitude: Double, longitude: Double): WeatherModel
}