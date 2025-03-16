package aandrosov.city.app.ui

import aandrosov.city.app.ui.viewModels.LoginViewModel
import aandrosov.city.app.ui.viewModels.NewsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { NewsViewModel(get(), get()) }
}