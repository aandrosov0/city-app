package aandrosov.city.data.repositories

import aandrosov.city.data.models.CityModel

interface CityRepository {
    suspend fun getAll(): List<CityModel>
}