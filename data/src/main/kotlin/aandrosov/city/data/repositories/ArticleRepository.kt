package aandrosov.city.data.repositories

import aandrosov.city.data.models.ArticleContentModel

interface ArticleRepository {
    suspend fun getById(cityId: Long, articleId: Long): ArticleContentModel
}