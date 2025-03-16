package aandrosov.city.data.models

import java.time.LocalDateTime

data class NewsModel(
    val cityId: Long = 0,
    val categoryId: Long = 0,
    val title: String = "",
    val imageUrl: String = "",
    val publishedAt: LocalDateTime = LocalDateTime.now()
)

