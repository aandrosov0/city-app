package aandrosov.city.app.ui.states

data class NewsScreenState(
    val news: List<NewsState> = listOf(),
    val category: CategoryState = CategoryState.ALL,
    val categories: List<CategoryState> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)
