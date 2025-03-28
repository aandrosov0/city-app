package aandrosov.city.app.ui.states

import aandrosov.city.data.models.ArticleContentModel

data class ArticleContentState(
    val id: Long = 0,
    val blocks: List<ArticleBlockState> = emptyList()
)

fun ArticleContentModel.toState() = ArticleContentState(
    id = id,
    blocks = content.map { it.toState() }
)
