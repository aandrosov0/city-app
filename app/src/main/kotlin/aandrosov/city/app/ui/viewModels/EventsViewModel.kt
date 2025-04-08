package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.states.CategoryState
import aandrosov.city.app.ui.states.EventContentState
import aandrosov.city.app.ui.states.EventScreenState
import aandrosov.city.app.ui.states.EventState
import aandrosov.city.app.ui.states.toState
import aandrosov.city.data.exceptions.DataInternetException
import aandrosov.city.data.repositories.EventContentRepository
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

class EventsViewModel(
    private val appViewModel: AppViewModel,
    private val eventContentRepository: EventContentRepository,
    private val firestore: FirebaseFirestore = Firebase.firestore,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventScreenState())
    val uiState = _uiState.asStateFlow()

    private val eventsCategories = firestore.collection("event_categories")

    private var cityId = 0L
    init {
        viewModelScope.launch {
            appViewModel.uiState.collect {
                cityId = it.settings.cityId
            }
        }
    }

    fun fetchEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = true)
            val categories = eventsCategories
                .get()
                .await()
                .toObjects<CategoryState>()

            val events = firestore
                .collection("cities/$cityId/events")
                .get()
                .await()
                .toObjects<EventState>()

            _uiState.value = uiState.value.copy(isLoading = false, categories = categories, events = events)
        }
    }

    fun fetchEventContent(eventId: Long, providerUrl: String) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true, eventContent = EventContentState())
            try {
                val content = eventContentRepository.getById(cityId, eventId).map { it.toState() }
                val eventContent = EventContentState(
                    id = eventId,
                    providerUrl = providerUrl,
                    content = content
                )
                _uiState.value = uiState.value.copy(isLoading = false, eventContent = eventContent)
            } catch (_: DataInternetException) {
                _uiState.value = uiState.value.copy(isLoading = false, isError = true)
            }
        }
    }
}