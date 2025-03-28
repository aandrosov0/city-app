package aandrosov.city.app.ui

import aandrosov.city.app.ui.viewModels.ArticleViewModel
import aandrosov.city.app.ui.viewModels.LoginViewModel
import aandrosov.city.app.ui.viewModels.NewsViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { Firebase.firestore }
    viewModelOf(::LoginViewModel)
    viewModelOf(::NewsViewModel)
    viewModelOf(::ArticleViewModel)
}