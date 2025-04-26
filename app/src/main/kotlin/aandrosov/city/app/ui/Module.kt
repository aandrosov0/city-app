package aandrosov.city.app.ui

import aandrosov.city.app.ui.viewModels.AppViewModel
import aandrosov.city.app.ui.viewModels.ArticleViewModel
import aandrosov.city.app.ui.viewModels.EventsViewModel
import aandrosov.city.app.ui.viewModels.LoginViewModel
import aandrosov.city.app.ui.viewModels.MenuViewModel
import aandrosov.city.app.ui.viewModels.NewsViewModel
import aandrosov.city.app.ui.viewModels.TicketsViewModel
import aandrosov.city.app.ui.viewModels.WeatherViewModel
import androidx.preference.PreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val appModule = module {
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    singleOf(::AppSettings)
    singleOf(::AppMemoryStorage)

    viewModelOf(::AppViewModel)
    viewModelOf(::NewsViewModel)
    viewModelOf(::ArticleViewModel)
    viewModelOf(::WeatherViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::NewsViewModel)
    viewModelOf(::EventsViewModel)
    viewModelOf(::TicketsViewModel)
    viewModelOf(::MenuViewModel)
}