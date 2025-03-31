package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.CityState
import aandrosov.city.app.ui.states.MenuScreenState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val appViewModel: AppViewModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(MenuScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            appViewModel.uiState.collect {
                _uiState.value = MenuScreenState(
                    currentCity = it.cities.find { city -> city.id  == it.settings.cityId } ?: CityState(),
                    cities = it.cities,
                    isDarkModeEnabled = it.settings.isDarkModeEnabled
                )
            }
        }
    }

    fun quit() = appViewModel.quitAccount()

    fun updateSettings(
        city: CityState = uiState.value.currentCity,
        isDarkModeEnabled: Boolean = uiState.value.isDarkModeEnabled,
    ) {
        appViewModel.updateSettings(
            cityId = city.id,
            isDarkModeEnabled = isDarkModeEnabled
        )
    }
}