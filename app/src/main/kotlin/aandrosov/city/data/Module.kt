package aandrosov.city.data

import aandrosov.city.data.repositories.CityRepository
import aandrosov.city.data.repositories.CityRepositoryImpl
import aandrosov.city.data.repositories.NewsRepository
import aandrosov.city.data.repositories.NewsRepositoryImpl
import aandrosov.city.data.repositories.SettingsRepository
import aandrosov.city.data.repositories.SettingsRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl() }
    single<NewsRepository> { NewsRepositoryImpl() }
    single<CityRepository> { CityRepositoryImpl() }
}