package aandrosov.city.app.ui.states

data class NewsScreenState(
    val categories: List<CategoryState> = listOf(),
    val news: List<NewsState> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
