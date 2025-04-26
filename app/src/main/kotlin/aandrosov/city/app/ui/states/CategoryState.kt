package aandrosov.city.app.ui.states

import aandrosov.city.app.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

data class CategoryState(
    val id: Long = 0,
    val title: String = ""
) {
    companion object {
        val ALL = CategoryState(
            id = -1,
            title = ""
        )

        val FAVORITE = CategoryState(
            id = -2,
            title = ""
        )
    }

    override fun toString() = title
}

val CategoryState.localizedTitle: String
    @Composable get() = when (id) {
        CategoryState.ALL.id -> stringResource(R.string.category_all)
        CategoryState.FAVORITE.id -> stringResource(R.string.category_favorite)
        else -> title
    }