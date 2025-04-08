package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.WeatherScreenState
import aandrosov.city.app.ui.states.toState
import aandrosov.city.data.exceptions.DataInternetException
import aandrosov.city.data.repositories.WeatherRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class WeatherViewModel(
    appViewModel: AppViewModel,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeatherScreenState())
    val uiState = _uiState.asStateFlow()

    private var fetchingForecastJob: Job? = null

    private val cityId = appViewModel.uiState.value.settings.cityId

    fun fetchForecast() {
        fetchingForecastJob?.cancel()
        fetchingForecastJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = true)
            val cityRef = Firebase.firestore.document("/cities/$cityId")

            val city = cityRef.get().await()
            val longitude = city.getDouble("longitude") ?: .0
            val latitude = city.getDouble("latitude") ?: .0

            try {
                val forecast = weatherRepository.getForecast(latitude, longitude)
                _uiState.value = uiState.value.copy(
                    isLoading = false,
                    weather = forecast.toState()
                )
            } catch (_: DataInternetException) {
                _uiState.value = uiState.value.copy(isLoading = false, isError = true)
            }
        }
    }
}