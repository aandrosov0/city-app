package aandrosov.city.app.ui.states

data class EventState(
    val id: Long = 0,
    val categoryId: Long = 0,
    val title: String = "",
    val imageUrl: String = "",
    val price: String = "",
    val date: String = "",
    val providerUrl: String = "",
    val isFavorite: Boolean = false,
)
