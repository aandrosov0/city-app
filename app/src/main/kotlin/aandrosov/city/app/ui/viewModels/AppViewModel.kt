package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.AppMemoryStorage
import aandrosov.city.app.ui.AppSettings
import aandrosov.city.app.ui.states.AppState
import aandrosov.city.app.ui.states.CityState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

const val CITIES_MEMORY_KEY = "app_cities"

class AppViewModel(
    private val appSettings: AppSettings,
    private val appMemoryStorage: AppMemoryStorage,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AppState())

    val uiState = _uiState.asStateFlow()

    private val citiesRef = Firebase.firestore.collection("cities")

    init {
        appSettings.registerOnSettingsChangeListener(::onSettingsChanged)
        loadInternetData()
    }

    private fun onSettingsChanged() {
        _uiState.value = uiState.value.copy(
            isAuthorized = appSettings.cityId != 0L,
            isDarkModeEnabled = appSettings.isDarkModeEnabled
        )
    }

    fun loadInternetData() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = false)

            val cities = citiesRef
                .get()
                .await()
                .toObjects<CityState>()

            appMemoryStorage[CITIES_MEMORY_KEY] = cities

            _uiState.value = uiState.value.copy(isLoading = false)
        }
    }

    override fun onCleared() {
        appSettings.unregisterOnSettingsChangeListener(::onSettingsChanged)
    }
}