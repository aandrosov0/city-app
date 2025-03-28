package aandrosov.city.data.models

sealed class ArticleBlockModel

data class ArticleThumbnailModel(
    val url: String,
    val caption: String,
) : ArticleBlockModel()

data class ArticleHeadlineModel(
    val text: String
) : ArticleBlockModel()

data class ArticleSubheadlineModel(
    val text: String
) : ArticleBlockModel()

data class ArticleParagraphModel(
    val text: String
) : ArticleBlockModel()