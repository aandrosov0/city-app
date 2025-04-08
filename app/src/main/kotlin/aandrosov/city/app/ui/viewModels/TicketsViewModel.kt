package aandrosov.city.app.ui.viewModels

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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TicketsViewModel(
    private val appViewModel: AppViewModel,
    private val ticketContentRepository: TicketContentRepository,
    private val firestore: FirebaseFirestore = Firebase.firestore,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketsScreenState())
    val uiState = _uiState.asStateFlow()

    private val ticketCategories = firestore.collection("ticket_categories")

    private var cityId = 0L
    init {
        viewModelScope.launch {
            appViewModel.uiState.collect {
                cityId = it.settings.cityId
            }
        }
    }

    fun fetchTickets() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = uiState.value.copy(isLoading = true)
            val categories = ticketCategories
                .get()
                .await()
                .toObjects<CategoryState>()

            val tickets = firestore
                .collection("cities/$cityId/tickets")
                .get()
                .await()
                .toObjects<TicketState>()

            _uiState.value = uiState.value.copy(isLoading = false, tickets = tickets, categories = categories,)
        }
    }

    fun fetchTicketContent(ticketId: Long, providerUrl: String) {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(isLoading = true, ticketContent = TicketContentState())
            try {
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
}