package aandrosov.city.data.repositories

import aandrosov.city.data.ArticleRenderer
import aandrosov.city.data.models.ArticleBlockModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.commonmark.parser.Parser

class TicketContentRepositoryImpl(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TicketContentRepository {
    companion object {
        private const val TICKETS_URL = "https://raw.githubusercontent.com/aandrosov0/city-app-contents/master/city-%s/tickets/%s.md"
    }

    override suspend fun getById(
        cityId: Long,
        ticketId: Long
    ): List<ArticleBlockModel> = withContext(dispatcher) {
        val response = httpClient.get(TICKETS_URL.format(cityId, ticketId))
        val body = response.bodyAsText()

        val markdownParser = Parser.builder().build()
        val document = markdownParser.parse(body)
        val articleRenderer = ArticleRenderer()
        articleRenderer.render(document)
    }
}