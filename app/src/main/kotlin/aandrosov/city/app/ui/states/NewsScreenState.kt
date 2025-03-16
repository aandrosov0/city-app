package aandrosov.city.app.ui.states

data class NewsScreenState(
    val news: List<NewsState> = listOf(),
    val isLoading: Boolean = false
)
