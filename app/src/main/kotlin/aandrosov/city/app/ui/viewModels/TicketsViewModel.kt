package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.AppMemoryStorage
import aandrosov.city.app.AppSettings
import aandrosov.city.app.ui.states.CategoryState
import aandrosov.city.app.ui.states.TicketContentState
import aandrosov.city.app.ui.states.TicketState
import aandrosov.city.app.ui.states.TicketsScreenState
import aandrosov.city.app.ui.states.toState
import aandrosov.city.data.exceptions.DataInternetException
import aandrosov.city.data.repositories.TicketContentRepository
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

class TicketsViewModel(
    private val appSettings: AppSettings,
    private val appMemoryStorage: AppMemoryStorage,
    private val ticketContentRepository: TicketContentRepository,
) : ViewModel() {
    companion object {
        private const val TICKETS_MEMORY_KEY = "tickets_viewmodel_tickets"
        private const val CATEGORIES_MEMORY_KEY = "tickets_viewmodel_categories"
        private const val CATEGORY_MEMORY_KEY = "tickets_viewmodel_category"
    }

    private val _uiState = MutableStateFlow(TicketsScreenState())

    val uiState = _uiState.asStateFlow()
    private var fetchingTicketsJob: Job? = null

    private val ticketCategories = Firebase.firestore.collection("ticket_categories")

    init {
        val isCacheLoaded = loadCache()
        if (!isCacheLoaded) {
            fetchTickets()
        }

        appSettings.registerOnSettingsChangeListener(::updateFavorites)
    }

    fun fetchTickets(category: CategoryState = CategoryState.ALL) {
        fetchingTicketsJob?.cancel()
        fetchingTicketsJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = true)

            val categories = ticketCategories
                .get()
                .await()
                .toObjects<CategoryState>()

            val cityId = appSettings.cityId

            var ticketsRef: Query = Firebase.firestore
                .collection("cities/$cityId/tickets")

            ticketsRef = when (category.id) {
                CategoryState.ALL.id -> ticketsRef
                CategoryState.FAVORITE.id -> {
                    val favorites = appSettings.favoriteTickets.toMutableList().apply {
                        add(-1)
                    }

                    println(favorites)

                    ticketsRef.whereIn("id", favorites)
                }
                else -> ticketsRef.whereEqualTo("categoryId", category.id)
            }

            val tickets = ticketsRef.get().await().toObjects<TicketState>()

            appMemoryStorage[CATEGORIES_MEMORY_KEY] = categories
            appMemoryStorage[CATEGORY_MEMORY_KEY] = category
            appMemoryStorage[TICKETS_MEMORY_KEY] = tickets

            _uiState.value = uiState.value.copy(
                isLoading = false, tickets = tickets,
                categories = categories, category = category
            )
            updateFavorites()
        }
    }

    fun fetchTicketContent(ticketId: Long, providerUrl: String) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true, ticketContent = TicketContentState())
            try {
                val cityId = appSettings.cityId
                val content = ticketContentRepository.getById(cityId, ticketId).map { it.toState() }
                val ticketContent = TicketContentState(
                    id = ticketId,
                    providerUrl = providerUrl,
                    content = content
                )
                _uiState.value = uiState.value.copy(isLoading = false, ticketContent = ticketContent)
            } catch (_: DataInternetException) {
                _uiState.value = uiState.value.copy(isLoading = false, isError = true)
            }
        }
    }

    fun toggleFavorite(ticketId: Long) {
        val ids = appSettings.favoriteTickets.toMutableSet()

        if (!ids.remove(ticketId)) {
            ids.add(ticketId)
        }

        println(ids)

        appSettings.update(favoriteTickets = ids)
    }

    override fun onCleared() {
        appSettings.unregisterOnSettingsChangeListener(::updateFavorites)
    }

    @Suppress("UNCHECKED_CAST")
    private fun loadCache(): Boolean {
        val categories = appMemoryStorage[CATEGORIES_MEMORY_KEY] as? List<CategoryState>
        val tickets = appMemoryStorage[TICKETS_MEMORY_KEY] as? List<TicketState>
        val category = appMemoryStorage[CATEGORY_MEMORY_KEY] as? CategoryState

        val isNotEmpty = categories != null && tickets != null && category != null
        if (isNotEmpty) {
            _uiState.value = uiState.value.copy(
                categories = categories, tickets = tickets,
                category = category, isLoading = false
            )
        }

        return isNotEmpty
    }

    private fun updateFavorites() {
        val favoriteTickets = appSettings.favoriteTickets

        _uiState.value = uiState.value.copy(
            tickets = uiState.value.tickets.map {
                val isFavorite = favoriteTickets.contains(it.id)
                it.copy(isFavorite = isFavorite)
            }
        )
    }
}