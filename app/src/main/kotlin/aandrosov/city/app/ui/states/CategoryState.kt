package aandrosov.city.app.ui.states

data class CategoryState(
    val id: Long = 0,
    val title: String = ""
) {
    override fun toString() = title
}