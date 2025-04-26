package aandrosov.city.app.ui.states

import java.time.Instant
import java.util.Date

data class NewsState(
    val id: Long = 0,
    val cityId: Long = 0,
    val categoryId: Long = 0,
    val title: String = "",
    val imageUrl: String = "",
    val publishedAt: Date = Date.from(Instant.now()),
    val isFavorite: Boolean = false,
)