package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.AppState
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(
    preferences: SharedPreferences
) : ViewModel() {
    companion object {
        private const val CITY_ID_KEY = "CITY_ID"
    }

    private val _uiState = MutableStateFlow(AppState())

    val uiState = _uiState.asStateFlow()

    private val prefEditor = preferences.edit()

    init {
        preferences.registerOnSharedPreferenceChangeListener { preferences, _ ->
            onSharedPreferencesChanged(preferences)
        }
        onSharedPreferencesChanged(preferences)
    }

    fun updateSettings(cityId: Long = _uiState.value.settings.cityId) {
        prefEditor
            .putLong(CITY_ID_KEY, cityId)
            .apply()
    }

    private fun onSharedPreferencesChanged(preferences: SharedPreferences) {
        val cityId = preferences.getLong(CITY_ID_KEY, 0)

        _uiState.value = uiState.value.copy(
            isAuthorized = cityId != 0L,
            settings = _uiState.value.settings.copy(
                cityId = cityId
            )
        )
    }
}