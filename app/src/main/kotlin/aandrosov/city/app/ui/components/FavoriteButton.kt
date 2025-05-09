package aandrosov.city.app.ui.components

import aandrosov.city.app.R
import aandrosov.city.app.ui.themes.AppTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

data class FavoriteButtonColors(
    val activeColor: Color,
    val inactiveColor: Color,
)

object FavoriteButtonDefaults {
    @Composable
    fun colors(
        activeColor: Color = MaterialTheme.colorScheme.primary,
        inactiveColor: Color = MaterialTheme.colorScheme.onBackground
    ) = FavoriteButtonColors(
        activeColor = activeColor,
        inactiveColor = inactiveColor
    )
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: FavoriteButtonColors = FavoriteButtonDefaults.colors()
) {
    val contentColor = if (isFavorite) {
        colors.activeColor
    } else {
        colors.inactiveColor
    }

    val iconId = if (isFavorite) {
        R.drawable.ic_favorite_filled
    } else {
        R.drawable.ic_favorite
    }

    IconButton(
        onClick = onClick,
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = contentColor
        )
    ) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = stringResource(R.string.favorite),
        )
    }
}

@Preview
@Composable
private fun FavoriteButtonPreview() = AppTheme {
    var isFavorite by remember { mutableStateOf(false) }
    FavoriteButton(
        isFavorite = isFavorite,
        onClick = { isFavorite = !isFavorite }
    )
}