package aandrosov.city.data.repositories

import aandrosov.city.data.models.SettingsModel

class SettingsRepositoryImpl : SettingsRepository {
    private var settings = SettingsModel(
        cityId = 1
    )

    override suspend fun get() = settings

    override suspend fun saveAll(settings: SettingsModel) {
        this.settings = settings
    }
}