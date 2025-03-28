package aandrosov.city.app.ui.states

import aandrosov.city.data.models.ArticleBlockModel
import aandrosov.city.data.models.ArticleHeadlineModel
import aandrosov.city.data.models.ArticleParagraphModel
import aandrosov.city.data.models.ArticleSubheadlineModel
import aandrosov.city.data.models.ArticleThumbnailModel

sealed class ArticleBlockState

data class ArticleHeadlineState(
    val text: String = ""
) : ArticleBlockState()

data class ArticleSubheadlineState(
    val text: String = ""
) : ArticleBlockState()

data class ArticleParagraphState(
    val text: String = ""
) : ArticleBlockState()

data class ArticleThumbnailState(
    val url: String = "",
    val caption: String = "",
) : ArticleBlockState()

fun ArticleBlockModel.toState(): ArticleBlockState = when (this) {
    is ArticleHeadlineModel -> toState()
    is ArticleParagraphModel -> toState()
    is ArticleSubheadlineModel -> toState()
    is ArticleThumbnailModel -> toState()
}

fun ArticleHeadlineModel.toState() = ArticleHeadlineState(
    text = text
)

fun ArticleSubheadlineModel.toState() = ArticleSubheadlineState(
    text = text
)

fun ArticleParagraphModel.toState() = ArticleParagraphState(
    text = text
)

fun ArticleThumbnailModel.toState() = ArticleThumbnailState(
    url = url,
    caption = caption
)