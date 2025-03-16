package aandrosov.city.app.ui.states

import aandrosov.city.data.models.CityModel

data class CityState(
    val id: Long = 0,
    val title: String = ""
) {
    override fun toString() = title
}


fun CityModel.toState() = CityState(
    id = id,
    title = title
)
