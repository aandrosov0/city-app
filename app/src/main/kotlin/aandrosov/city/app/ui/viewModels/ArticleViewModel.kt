package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.AppSettings
import aandrosov.city.app.ui.navigation.Article
import aandrosov.city.app.ui.states.ArticleScreenState
import aandrosov.city.app.ui.states.toState
import aandrosov.city.data.exceptions.DataInternetException
import aandrosov.city.data.repositories.ArticleRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleViewModel(
    handle: SavedStateHandle,
    private val appSettings: AppSettings,
    private val articleRepository: ArticleRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleScreenState())
    val uiState = _uiState.asStateFlow()

    private val articleId = handle.toRoute<Article>().id
    fun fetch() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val cityId = appSettings.cityId
                val article = articleRepository.getById(cityId, articleId).toState()
                _uiState.value = ArticleScreenState(article = article)
            } catch (_: DataInternetException) {
                _uiState.value = uiState.value.copy(isLoading = false, isError = true)
            }
        }
    }
}