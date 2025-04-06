package aandrosov.city.data.repositories

import aandrosov.city.data.ArticleRenderer
import aandrosov.city.data.models.ArticleContentModel
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.commonmark.parser.Parser

class ArticleRepositoryImpl(
    private val httpClient: HttpClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ArticleRepository {
    companion object {
        private const val ARTICLES_URL = "https://raw.githubusercontent.com/aandrosov0/city-app-contents/master/city-%s/articles/%s.md"
    }

    override suspend fun getById(cityId: Long, articleId: Long): ArticleContentModel = withContext(dispatcher) {
        val response = httpClient.get(ARTICLES_URL.format(cityId, articleId))
        val body = response.bodyAsText()

        val markdownParser = Parser.builder().build()
        val document = markdownParser.parse(body)
        val articleRenderer = ArticleRenderer()

        println(ARTICLES_URL.format(cityId, articleId))

        ArticleContentModel(
            id = articleId,
            content = articleRenderer.render(document)
        )
    }
}