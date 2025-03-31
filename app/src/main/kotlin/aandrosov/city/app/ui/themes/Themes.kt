package aandrosov.city.app.ui.themes

import aandrosov.city.app.adaptToTheme
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val lightColorScheme
    get() = lightColorScheme(
        primary = primaryColor,
        onPrimary = onPrimaryColor,
        secondary = secondaryColor,
        onSecondary = onSecondaryColor,
        background = backgroundColor,
        onBackground = onBackgroundColor
    )

private val darkColorScheme
    get() = lightColorScheme.copy(
        background = darkBackgroundColor,
        onBackground = onDarkBackgroundColor
    )

private val typography
    @Composable get() = MaterialTheme.typography

val shapes
    get() = Shapes(
        extraSmall = RoundedCornerShape(8.dp)
    )

@Composable
fun AppTheme(
    isDarkModeEnabled: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkModeEnabled) {
        darkColorScheme
    } else {
        lightColorScheme
    }

    LocalActivity.current?.adaptToTheme(isDarkModeEnabled)

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
        content = content
    )
}