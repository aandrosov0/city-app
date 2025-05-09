package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.AppMemoryStorage
import aandrosov.city.app.AppSettings
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
import com.google.firebase.firestore.Query
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
        private const val CATEGORY_MEMORY_KEY = "category_viewmodel_events"
    }

    private val _uiState = MutableStateFlow(EventScreenState())
    val uiState = _uiState.asStateFlow()

    private val eventsCategories = Firebase.firestore.collection("event_categories")

    private var fetchingJob: Job? = null

    init {
        appSettings.registerOnSettingsChangeListener(::updateFavorites)

        val isCacheLoaded = loadCache()

        if (!isCacheLoaded) {
            fetchEvents()
        }
    }

    fun fetchEvents(category: CategoryState = CategoryState.ALL) {
        fetchingJob?.cancel()
        fetchingJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = true)

            val categories = eventsCategories
                .get()
                .await()
                .toObjects<CategoryState>()

            var eventsRef: Query = Firebase.firestore
                .collection("cities/${appSettings.cityId}/events")

            eventsRef = when (category.id) {
                CategoryState.ALL.id -> eventsRef
                CategoryState.FAVORITE.id -> {
                    val favorites = appSettings.favoriteEvents.toMutableList().apply {
                        add(-1)
                    }

                    println(favorites)

                    eventsRef.whereIn("id", favorites)
                }
                else -> eventsRef.whereEqualTo("categoryId", category.id)
            }

            val events = eventsRef
                .get()
                .await()
                .toObjects<EventState>()

            appMemoryStorage[EVENTS_MEMORY_KEY] = events
            appMemoryStorage[CATEGORY_MEMORY_KEY] = category
            appMemoryStorage[CATEGORIES_MEMORY_KEY] = categories

            _uiState.value = uiState.value.copy(
                isLoading = false, categories = categories,
                category = category, events = events
            )

            updateFavorites()
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

    fun toggleFavorite(eventId: Long) {
        val ids = appSettings.favoriteEvents.toMutableSet()

        if (!ids.remove(eventId)) {
            ids.add(eventId)
        }

        appSettings.update(favoriteEvents = ids)
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadCache(): Boolean {
        val events = appMemoryStorage[EVENTS_MEMORY_KEY] as? List<EventState>
        val categories = appMemoryStorage[CATEGORIES_MEMORY_KEY] as? List<CategoryState>
        val category = appMemoryStorage[CATEGORY_MEMORY_KEY] as? CategoryState

        val isNotEmpty = events != null && categories != null && category != null
        if (isNotEmpty) {
            _uiState.value = uiState.value.copy(
                events = events, categories = categories,
                category = category, isLoading = false
            )
        }

        return isNotEmpty
    }

    private fun updateFavorites() {
        val favoriteEvents = appSettings.favoriteEvents

        _uiState.value = uiState.value.copy(
            events = uiState.value.events.map {
                val isFavorite = favoriteEvents.contains(it.id)
                it.copy(isFavorite = isFavorite)
            }
        )
    }
}