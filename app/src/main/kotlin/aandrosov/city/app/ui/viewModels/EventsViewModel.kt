package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.AppMemoryStorage
import aandrosov.city.app.ui.AppSettings
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
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EventsViewModel(
    private val appSettings: AppSettings,
    private val appMemoryStorage: AppMemoryStorage,
    private val eventContentRepository: EventContentRepository,
) : ViewModel() {
    companion object {
        private const val EVENTS_MEMORY_KEY = "events_viewmodel_events"
        private const val CATEGORIES_MEMORY_KEY = "categories_viewmodel_events"
    }

    private val _uiState = MutableStateFlow(EventScreenState())
    val uiState = _uiState.asStateFlow()

    private val eventsCategories = Firebase.firestore.collection("event_categories")

    private var fetchingJob: Job? = null

    init {
        val isCacheLoaded = loadCache()

        if (!isCacheLoaded) {
            fetchEvents()
        }
    }

    fun fetchEvents() {
        fetchingJob?.cancel()
        fetchingJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = true)
            val categories = eventsCategories
                .get()
                .await()
                .toObjects<CategoryState>()

            val events = Firebase.firestore
                .collection("cities/${appSettings.cityId}/events")
                .get()
                .await()
                .toObjects<EventState>()

            appMemoryStorage[EVENTS_MEMORY_KEY] = events
            appMemoryStorage[CATEGORIES_MEMORY_KEY] = categories

            _uiState.value = uiState.value.copy(isLoading = false, categories = categories, events = events)
        }
    }

    fun fetchEventContent(eventId: Long, providerUrl: String) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true, eventContent = EventContentState())
            _uiState.value = try {
                val content = eventContentRepository.getById(appSettings.cityId, eventId).map { it.toState() }
                val eventContent = EventContentState(
                    id = eventId,
                    providerUrl = providerUrl,
                    content = content
                )
                uiState.value.copy(isLoading = false, eventContent = eventContent)
            } catch (_: DataInternetException) {
                uiState.value.copy(isLoading = false, isError = true)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadCache(): Boolean {
        val events = appMemoryStorage[EVENTS_MEMORY_KEY] as? List<EventState>
        val categories = appMemoryStorage[CATEGORIES_MEMORY_KEY] as? List<CategoryState>

        val isNotEmpty = events != null && categories != null
        if (isNotEmpty) {
            _uiState.value = uiState.value.copy(
                events = events, categories = categories,
                isLoading = false
            )
        }

        return isNotEmpty
    }
}