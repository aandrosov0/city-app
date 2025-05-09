package aandrosov.city.app.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun Modifier.shimmer(
    backgroundColor: Color = Color(0xFFBDBDBD),
    shimmerColor: Color = Color.White
): Modifier {
    val infiniteTransition = rememberInfiniteTransition()
    val offset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart
        )
    )

    val gradientBrush = Brush.linearGradient(
        offset to backgroundColor,
        offset + 0.1f to shimmerColor,
        offset + 0.2f to backgroundColor,
    )
    return drawBehind {
        drawRect(
            brush = gradientBrush,
            topLeft = Offset(0f, 0f)
        )
    }
}