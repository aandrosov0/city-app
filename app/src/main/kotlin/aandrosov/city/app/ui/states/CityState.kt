package aandrosov.city.app.ui.states

data class CityState(
    val id: Long = 0,
    val title: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
) {
    override fun toString() = title
}