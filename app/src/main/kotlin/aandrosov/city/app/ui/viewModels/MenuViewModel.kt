package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.AppMemoryStorage
import aandrosov.city.app.ui.AppSettings
import aandrosov.city.app.ui.states.CityState
import aandrosov.city.app.ui.states.MenuScreenState
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MenuViewModel(
    private val appSettings: AppSettings,
    private val appMemoryStorage: AppMemoryStorage,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuScreenState())
    val uiState = _uiState.asStateFlow()

    @Suppress("UNCHECKED_CAST")
    val cities = appMemoryStorage[CITIES_MEMORY_KEY] as List<CityState>

    init {
        _uiState.value = MenuScreenState(
            currentCity = cities.find { city -> city.id  == appSettings.cityId } ?: CityState(),
            cities = cities
        )

        appSettings.registerOnSettingsChangeListener(::onSettingsChanged)
    }

    fun quit() {
        onCleared()
        appSettings.update(cityId = 0)
    }

    fun updateSettings(
        city: CityState = uiState.value.currentCity,
        isDarkModeEnabled: Boolean = uiState.value.isDarkModeEnabled,
    ) {
        if (appSettings.cityId != city.id) {
            appMemoryStorage.clear()
            appMemoryStorage[CITIES_MEMORY_KEY] = cities
        }

        appSettings.update(
            cityId = city.id,
            isDarkModeEnabled = isDarkModeEnabled
        )
    }

    override fun onCleared() {
        appSettings.unregisterOnSettingsChangeListener(::onSettingsChanged)
    }

    private fun onSettingsChanged() {
        _uiState.value = uiState.value.copy(
            currentCity = cities.find { it.id == appSettings.cityId }!!,
            isDarkModeEnabled = appSettings.isDarkModeEnabled
        )
    }
}