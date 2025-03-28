package aandrosov.city.data

import aandrosov.city.data.repositories.ArticleRepository
import aandrosov.city.data.repositories.ArticleRepositoryImpl
import aandrosov.city.data.repositories.SettingsRepository
import aandrosov.city.data.repositories.SettingsRepositoryImpl
import org.koin.dsl.module

val dataModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl() }
    single<ArticleRepository> { ArticleRepositoryImpl() }
}