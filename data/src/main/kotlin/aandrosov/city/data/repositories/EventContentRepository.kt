package aandrosov.city.data.repositories

import aandrosov.city.data.models.ArticleBlockModel

interface EventContentRepository {
    suspend fun getById(cityId: Long, eventId: Long): List<ArticleBlockModel>
}