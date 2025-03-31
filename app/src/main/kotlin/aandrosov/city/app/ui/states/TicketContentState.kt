package aandrosov.city.app.ui.states

data class TicketContentState(
    val id: Long = 0,
    val providerUrl: String = "",
    val content: List<ArticleBlockState> = emptyList()
)
