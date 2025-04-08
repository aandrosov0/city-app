package aandrosov.city.app.ui.states

data class ArticleScreenState(
    val isLoading: Boolean = false,
    val article: ArticleContentState = ArticleContentState(),
    val isError: Boolean = false,
)
