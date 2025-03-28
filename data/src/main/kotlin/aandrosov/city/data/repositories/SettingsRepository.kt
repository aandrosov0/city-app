package aandrosov.city.data.repositories

import aandrosov.city.data.models.SettingsModel

interface SettingsRepository {
    suspend fun get(): SettingsModel
    suspend fun saveAll(settings: SettingsModel)
}