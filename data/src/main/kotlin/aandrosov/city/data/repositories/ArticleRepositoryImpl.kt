package aandrosov.city.data.repositories

import aandrosov.city.data.ArticleRenderer
import aandrosov.city.data.exceptions.DataInternetException
import aandrosov.city.data.models.ArticleContentModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import org.commonmark.parser.Parser

class ArticleRepositoryImpl(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ArticleRepository {
    companion object {
        private const val ARTICLES_URL = "https://raw.githubusercontent.com/aandrosov0/city-app-contents/master/city-%s/articles/%s.md"
    }

    override suspend fun getById(cityId: Long, articleId: Long): ArticleContentModel = withContext(dispatcher) {
        val response = try {
            httpClient.get(ARTICLES_URL.format(cityId, articleId))
        } catch (_: IOException) {
            throw DataInternetException()
        }

        val body = response.bodyAsText()

        val markdownParser = Parser.builder().build()
        val document = markdownParser.parse(body)
        val articleRenderer = ArticleRenderer()

        ArticleContentModel(
            id = articleId,
            content = articleRenderer.render(document)
        )
    }
}