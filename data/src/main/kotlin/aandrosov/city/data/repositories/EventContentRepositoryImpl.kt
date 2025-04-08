package aandrosov.city.data.repositories

import aandrosov.city.data.ArticleRenderer
import aandrosov.city.data.exceptions.DataInternetException
import aandrosov.city.data.models.ArticleBlockModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import org.commonmark.parser.Parser

class EventContentRepositoryImpl(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : EventContentRepository {
    companion object {
        private const val EVENTS_URL = "https://raw.githubusercontent.com/aandrosov0/city-app-contents/master/city-%s/events/%s.md"
    }

    override suspend fun getById(
        cityId: Long,
        eventId: Long
    ): List<ArticleBlockModel> = withContext(dispatcher) {
        val response = try {
            httpClient.get(EVENTS_URL.format(cityId, eventId))
        } catch (_: IOException) {
            throw DataInternetException()
        }

        val body = response.bodyAsText()

        val markdownParser = Parser.builder().build()
        val document = markdownParser.parse(body)
        val articleRenderer = ArticleRenderer()
        articleRenderer.render(document)
    }
}