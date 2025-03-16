package aandrosov.city.app.ui.components

import aandrosov.city.app.ui.themes.AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun <T : Any> RowSelector(
    current: T,
    items: List<T>,
    onItemSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items.forEach { item ->
            RowItem(
                label = "$item",
                selected = current == item,
                onSelect = { onItemSelect(item) },
            )
        }
    }
}

@Composable
private fun RowItem(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onSelect),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            color = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(
            Modifier
                .height(2.dp)
                .background(MaterialTheme.colorScheme.primary)
                .alpha(if (selected) 1f else 0f)
        )
    }
}

@Preview
@Composable
private fun RowSelectorPreview() = AppTheme {
    RowSelector(
        current = "Все",
        items = listOf("Все", "Общество", "Происшествия", "От первого лица"),
        onItemSelect = {}
    )
}