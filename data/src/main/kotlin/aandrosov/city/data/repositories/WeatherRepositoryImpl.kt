package aandrosov.city.data.repositories

import aandrosov.city.data.models.WeatherModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : WeatherRepository {
    companion object {
        const val API_URL = "https://api.open-meteo.com/v1/forecast"
    }

    override suspend fun getForecast(latitude: Double, longitude: Double) = withContext(dispatcher) {
        val url = "$API_URL?latitude=$latitude&longitude=$longitude&current=temperature_2m,weather_code"
        val response = httpClient.get(url)
        response.body<WeatherModel>()
    }
}