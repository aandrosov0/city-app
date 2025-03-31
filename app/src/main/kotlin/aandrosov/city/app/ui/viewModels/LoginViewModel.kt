package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.CityState
import aandrosov.city.app.ui.states.LoginScreenState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val appViewModel: AppViewModel) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            appViewModel.uiState.collect {
                _uiState.value = uiState.value.copy(
                    current = it.cities.firstOrNull() ?: CityState(),
                    cities = it.cities
                )
            }
        }
    }

    fun selectCity(city: CityState) {
        appViewModel.updateSettings(cityId = city.id)
        _uiState.value.copy(current = city)
    }
}