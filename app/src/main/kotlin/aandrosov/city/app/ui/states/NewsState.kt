package aandrosov.city.app.ui.states

import aandrosov.city.data.models.NewsModel
import java.time.LocalDateTime

data class NewsState(
    val cityId: Long = 0,
    val categoryId: Long = 0,
    val title: String = "",
    val imageUrl: String = "",
    val publishedAt: LocalDateTime = LocalDateTime.now(),
)

fun NewsModel.toState() = NewsState(
    cityId = cityId,
    categoryId = categoryId,
    title = title,
    imageUrl = imageUrl,
    publishedAt = publishedAt
)