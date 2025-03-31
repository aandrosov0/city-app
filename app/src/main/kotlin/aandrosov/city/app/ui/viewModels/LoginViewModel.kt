package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.CityState
import aandrosov.city.app.ui.states.LoginScreenState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val appViewModel: AppViewModel,
    firebase: FirebaseFirestore = Firebase.firestore,
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState = _uiState.asStateFlow()

    private var fetchingJob: Job? = null

    private val citiesRef = firebase.collection("cities")

    fun fetch() {
        fetchingJob?.cancel()
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val cities = citiesRef.get().await().toObjects<CityState>()
            _uiState.value = _uiState.value.copy(cities = cities, isLoading = false)
        }
    }

    fun selectCity(id: Long) {
        appViewModel.updateSettings(cityId = id)
    }
}