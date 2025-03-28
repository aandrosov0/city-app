package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.navigation.Article
import aandrosov.city.app.ui.states.ArticleScreenState
import aandrosov.city.app.ui.states.toState
import aandrosov.city.data.repositories.ArticleRepository
import aandrosov.city.data.repositories.SettingsRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArticleViewModel(
    handle: SavedStateHandle,
    private val articleRepository: ArticleRepository,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ArticleScreenState())
    val uiState = _uiState.asStateFlow()

    private val articleId = handle.toRoute<Article>().id

    fun fetch() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val cityId = settingsRepository.get().cityId
            val article = articleRepository.getById(cityId, articleId).toState()
            _uiState.value = ArticleScreenState(article = article)
        }
    }
}