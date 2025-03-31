package aandrosov.city.app.ui.components

import aandrosov.city.app.R
import aandrosov.city.app.ui.themes.AppTheme
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


data class DropdownTextFieldColors(
    val borderColor: Color,
    val contentColor: Color,
)

object DropdownTextFieldDefaults {

    @Composable
    fun colors(
        borderColor: Color = MaterialTheme.colorScheme.secondary,
        contentColor: Color = MaterialTheme.colorScheme.secondary,
    ) = DropdownTextFieldColors(
        borderColor = borderColor,
        contentColor = contentColor
    )
}

@Composable
fun <T: Any> DropdownTextField(
    current: T,
    elements: List<T>,
    onElementSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    colors: DropdownTextFieldColors = DropdownTextFieldDefaults.colors(),
    contentPadding: PaddingValues = PaddingValues(14.dp, 16.dp)
) {
    var expanded by remember { mutableStateOf(false) }

    val borderSize = 1.dp
    val borderShape = MaterialTheme.shapes.small
    Box(modifier.width(240.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(borderSize, colors.borderColor, borderShape)
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CompositionLocalProvider(LocalContentColor provides colors.contentColor) {
                Text(
                    text = "$current",
                    modifier = Modifier.padding(contentPadding),
                    style = MaterialTheme.typography.labelLarge
                )
                val rotation by animateFloatAsState(if (!expanded) 0f else 180f)
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_drop_down),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(rotation)
                        .padding(start = 4.dp, end = 14.dp)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            elements.forEach { element ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "$element",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        onElementSelect(element)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun DropdownTextFieldPreview() = AppTheme {
    DropdownTextField(
        current = "1",
        elements = listOf("1", "2", "3"),
        onElementSelect = {}
    )
}