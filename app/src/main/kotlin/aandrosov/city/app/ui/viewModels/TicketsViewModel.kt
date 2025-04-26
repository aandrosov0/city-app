package aandrosov.city.app.ui.viewModels

import aandrosov.city.app.ui.AppMemoryStorage
import aandrosov.city.app.ui.AppSettings
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
    }

    fun fetchTickets() {
        fetchingTicketsJob?.cancel()
        fetchingTicketsJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = true)

            val categories = ticketCategories
                .get()
                .await()
                .toObjects<CategoryState>()

            val cityId = appSettings.cityId
            val tickets = Firebase.firestore
                .collection("cities/$cityId/tickets")
                .get()
                .await()
                .toObjects<TicketState>()

            appMemoryStorage[CATEGORIES_MEMORY_KEY] = categories
            appMemoryStorage[TICKETS_MEMORY_KEY] = tickets

            _uiState.value = uiState.value.copy(isLoading = false, tickets = tickets, categories = categories)
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

    @Suppress("UNCHECKED_CAST")
    private fun loadCache(): Boolean {
        val categories = appMemoryStorage[CATEGORIES_MEMORY_KEY] as? List<CategoryState>
        val tickets = appMemoryStorage[TICKETS_MEMORY_KEY] as? List<TicketState>

        val isNotEmpty = categories != null && tickets != null
        if (isNotEmpty) {
            _uiState.value = uiState.value.copy(
                categories = categories, tickets = tickets,
                isLoading = false
            )
        }

        return isNotEmpty
    }
}