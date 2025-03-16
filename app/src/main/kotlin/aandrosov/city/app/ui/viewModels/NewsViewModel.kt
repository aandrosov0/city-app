package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.NewsScreenState
import aandrosov.city.app.ui.states.toState
import aandrosov.city.data.repositories.NewsRepository
import aandrosov.city.data.repositories.SettingsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsViewModel(
    private val newsRepository: NewsRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsScreenState())
    val uiState = _uiState.asStateFlow()

    private var fetchingJob: Job? = null

    fun fetch() {
        fetchingJob?.cancel()
        fetchingJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val settings = settingsRepository.get()
            val news = newsRepository.getByCityId(settings.cityId).map { it.toState() }
            _uiState.value = _uiState.value.copy(news = news, isLoading = false)
        }
    }
}