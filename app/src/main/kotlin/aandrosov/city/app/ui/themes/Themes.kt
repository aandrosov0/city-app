package aandrosov.city.app.ui.themes

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    get() = Typography(
        headlineMedium = TextStyle(
            color = primaryColor,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFontFamily,
        ),
        headlineSmall = TextStyle(
            color = primaryColor,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFontFamily,
        ),
        titleMedium = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFontFamily,
        ),
        titleSmall = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFontFamily,
        ),
        bodyMedium = TextStyle(
            color = secondaryColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFontFamily,
        ),
        labelLarge = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = interFontFamily,
        ),
        labelMedium = TextStyle(
            fontSize = 14.sp,
            fontFamily = interFontFamily,
        ),
        labelSmall = TextStyle(
            fontSize = 10.sp,
            fontFamily = interFontFamily,
        ),
    )

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