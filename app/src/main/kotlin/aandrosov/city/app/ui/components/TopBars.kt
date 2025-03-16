package aandrosov.city.app.ui.components

import aandrosov.city.app.R
import aandrosov.city.app.ui.themes.AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BackTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
    ) {
        BackButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        AppLogo(Modifier.align(Alignment.Center))
    }
}

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
            Icon(
                painter = painterResource(R.drawable.ic_apartment),
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
private fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_arrow_left),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun BackTopBarPreview() = AppTheme {
    BackTopBar(onBackClick = {})
}