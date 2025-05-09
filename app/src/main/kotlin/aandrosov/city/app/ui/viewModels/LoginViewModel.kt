package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.AppMemoryStorage
import aandrosov.city.app.AppSettings
import aandrosov.city.app.ui.states.CityState
import aandrosov.city.app.ui.states.LoginScreenState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val appSettings: AppSettings,
    appMemoryStorage: AppMemoryStorage
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        @Suppress("UNCHECKED_CAST")
        val cities = appMemoryStorage[CITIES_MEMORY_KEY] as List<CityState>

        _uiState.value = uiState.value.copy(
            current = cities.firstOrNull() ?: CityState(),
            cities = cities
        )
    }

    fun selectCity(city: CityState) {
        appSettings.update(cityId = city.id)
        _uiState.value.copy(current = city)
    }
}