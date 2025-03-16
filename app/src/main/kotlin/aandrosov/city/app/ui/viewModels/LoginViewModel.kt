package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.CityState
import aandrosov.city.app.ui.states.LoginScreenState
import aandrosov.city.app.ui.states.toState
import aandrosov.city.data.repositories.CityRepository
import aandrosov.city.data.repositories.SettingsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val cityRepository: CityRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    private var fetchingJob: Job? = null

    fun fetch() {
        fetchingJob?.cancel()
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val cities = cityRepository.getAll().map { it.toState() }
            _uiState.value = _uiState.value.copy(cities = cities, isLoading = false)
        }
    }

    fun selectCity(city: CityState) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val settings = settingsRepository.get()
            settingsRepository.saveAll(settings.copy(cityId = city.id))
            _uiState.value = _uiState.value.copy(selectedCity = city, isLoading = false)
        }
    }
}