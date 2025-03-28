package aandrosov.city.app.ui.themes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
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

private val typography
    @Composable get() = MaterialTheme.typography

val shapes
    get() = Shapes(
        extraSmall = RoundedCornerShape(8.dp)
    )

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme,
        shapes = shapes,
        typography = typography,
        content = content
    )
}