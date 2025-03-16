package aandrosov.city.app.ui.components

import aandrosov.city.app.R
import aandrosov.city.app.ui.themes.AppTheme
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T: Any> DropdownTextField(
    current: T,
    elements: List<T>,
    onElementSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val borderSize = 1.dp
    val borderShape = MaterialTheme.shapes.small
    val borderColor = MaterialTheme.colorScheme.secondary

    Box(modifier.width(240.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(borderSize, borderColor, borderShape)
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.secondary) {
                Text(
                    text = "$current",
                    modifier = Modifier.padding(14.dp, 16.dp),
                    style = MaterialTheme.typography.labelLarge
                )
                val rotation by animateFloatAsState(if (!expanded) 0f else 180f)
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_drop_down),
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(rotation)
                        .padding(horizontal = 14.dp)
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
                    text = { Text("$element") },
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