package aandrosov.city.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FloatingPrice(
    price: String,
    modifier: Modifier = Modifier,
) {
    val containerColor = MaterialTheme.colorScheme.primary
    val containerShape = MaterialTheme.shapes.small
    val contentColor = MaterialTheme.colorScheme.onPrimary

    Text(
        text = price,
        modifier = modifier
            .background(containerColor, containerShape)
            .padding(horizontal = 16.dp, vertical = 2.dp),
        color = contentColor,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.labelLarge
    )
}