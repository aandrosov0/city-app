package aandrosov.city.data.repositories

import aandrosov.city.data.models.NewsModel

interface NewsRepository {
    suspend fun getByCityId(cityId: Long): List<NewsModel>
}