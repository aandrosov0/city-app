package aandrosov.city.data

import aandrosov.city.data.repositories.ArticleRepository
import aandrosov.city.data.repositories.ArticleRepositoryImpl
import aandrosov.city.data.repositories.EventContentRepository
import aandrosov.city.data.repositories.EventContentRepositoryImpl
import aandrosov.city.data.repositories.SettingsRepository
import aandrosov.city.data.repositories.SettingsRepositoryImpl
import aandrosov.city.data.repositories.TicketContentRepository
import aandrosov.city.data.repositories.TicketContentRepositoryImpl
import aandrosov.city.data.repositories.WeatherRepository
import aandrosov.city.data.repositories.WeatherRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single<SettingsRepository> { SettingsRepositoryImpl() }
    single<ArticleRepository> { ArticleRepositoryImpl(get()) }
    single<EventContentRepository> { EventContentRepositoryImpl(get()) }
    single<TicketContentRepository> { TicketContentRepositoryImpl(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
}