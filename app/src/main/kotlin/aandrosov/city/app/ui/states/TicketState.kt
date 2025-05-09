package aandrosov.city.app.ui.states

data class TicketState(
    val id: Long = 0,
    val categoryId: Long = 0,
    val title: String = "",
    val price: String = "",
    val imageUrl: String = "",
    val providerUrl: String = "",
    val isFavorite: Boolean = false
)
