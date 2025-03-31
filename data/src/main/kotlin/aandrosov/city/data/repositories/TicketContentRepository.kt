package aandrosov.city.data.repositories

import aandrosov.city.data.models.ArticleBlockModel

interface TicketContentRepository {
    suspend fun getById(cityId: Long, ticketId: Long): List<ArticleBlockModel>
}