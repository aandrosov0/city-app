package aandrosov.city.app.ui

import aandrosov.city.app.ui.viewModels.AppViewModel
import aandrosov.city.app.ui.viewModels.ArticleViewModel
import aandrosov.city.app.ui.viewModels.EventsViewModel
import aandrosov.city.app.ui.viewModels.LoginViewModel
import aandrosov.city.app.ui.viewModels.MenuViewModel
import aandrosov.city.app.ui.viewModels.NewsViewModel
import aandrosov.city.app.ui.viewModels.TicketsViewModel
import androidx.preference.PreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val appModule = module {
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
    single { AppViewModel(get()) }

    viewModelOf(::NewsViewModel)
    viewModelOf(::ArticleViewModel)

    viewModel { LoginViewModel(get()) }
    viewModel { NewsViewModel(get()) }
    viewModel { EventsViewModel(get(), get()) }
    viewModel { TicketsViewModel(get(), get()) }
    viewModel { MenuViewModel(get()) }
}