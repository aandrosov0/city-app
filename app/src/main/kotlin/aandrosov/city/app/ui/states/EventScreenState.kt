package aandrosov.city.app.ui.states

data class EventScreenState(
    val isLoading: Boolean = false,
    val events: List<EventState> = emptyList(),
    val categories: List<CategoryState> = emptyList(),
    val eventContent: EventContentState = EventContentState(),
    val isError: Boolean = false
)
