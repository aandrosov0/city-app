package aandrosov.city.app.ui.states

data class TicketsScreenState(
    val isLoading: Boolean = false,
    val tickets: List<TicketState> = emptyList(),
    val categories: List<CategoryState> = emptyList(),
    val ticketContent: TicketContentState = TicketContentState(),
    val isError: Boolean = false,
    val category: CategoryState = CategoryState.ALL
)
