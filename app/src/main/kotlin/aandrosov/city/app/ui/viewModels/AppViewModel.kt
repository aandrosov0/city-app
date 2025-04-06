package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.AppState
import aandrosov.city.app.ui.states.CityState
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AppViewModel(
    preferences: SharedPreferences,
    firebase: FirebaseFirestore = Firebase.firestore,
) : ViewModel() {
    companion object {
        private const val CITY_ID_KEY = "CITY_ID"
        private const val DARK_MODE_KEY = "DARK_MODE"
    }

    private val _uiState = MutableStateFlow(AppState())

    val uiState = _uiState.asStateFlow()

    private val citiesRef = firebase.collection("cities")
    private val prefEditor = preferences.edit()

    val onSharedPreferencesChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { pref, key ->
        onSharedPreferencesChanged(pref)
    }

    init {
        preferences.registerOnSharedPreferenceChangeListener(onSharedPreferencesChangeListener)
        onSharedPreferencesChanged(preferences)
        loadInternetData()
    }

    fun updateSettings(
        cityId: Long = _uiState.value.settings.cityId,
        isDarkModeEnabled: Boolean = uiState.value.settings.isDarkModeEnabled
    ) {
        prefEditor
            .putBoolean(DARK_MODE_KEY, isDarkModeEnabled)
            .putLong(CITY_ID_KEY, cityId)
            .apply()
    }

    fun quitAccount() {
        updateSettings(cityId = 0)
    }

    private fun onSharedPreferencesChanged(preferences: SharedPreferences) {
        val cityId = preferences.getLong(CITY_ID_KEY, 0)
        val isDarkModeEnabled = preferences.getBoolean(DARK_MODE_KEY, false)

        _uiState.value = uiState.value.copy(
            isAuthorized = cityId != 0L,
            settings = _uiState.value.settings.copy(
                cityId = cityId,
                isDarkModeEnabled = isDarkModeEnabled
            )
        )
    }

    private fun loadInternetData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = false)

            val cities = citiesRef
                .get()
                .await()
                .toObjects<CityState>()

            _uiState.value = uiState.value.copy(
                cities = cities,
                isLoading = false
            )
        }
    }
}