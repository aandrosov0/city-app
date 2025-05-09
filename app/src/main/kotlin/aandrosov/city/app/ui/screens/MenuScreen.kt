package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.redirectToBrowser
import aandrosov.city.app.ui.components.DropdownTextField
import aandrosov.city.app.ui.components.DropdownTextFieldDefaults
import aandrosov.city.app.ui.navigation.Weather
import aandrosov.city.app.ui.themes.AppTheme
import aandrosov.city.app.ui.viewModels.MenuViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun MenuScreen(
    onNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier,
    menuViewModel: MenuViewModel = koinViewModel()
) {
    val uiState by menuViewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 14.dp),
    ) {
        MenuButton(
            icon = painterResource(R.drawable.ic_weather),
            label = stringResource(R.string.weather_menu_button),
            onClick = { onNavigate(Weather) }
        )
        HorizontalDivider()
        Spacer(Modifier.height(32.dp))
        MenuDropDownTextField(
            icon = painterResource(R.drawable.ic_apartment),
            label = stringResource(R.string.city_menu_drop_down_text_field),
            current = uiState.currentCity,
            elements = uiState.cities,
            onElementSelected = { menuViewModel.updateSettings(city = it) }
        )
        HorizontalDivider()
        MenuSwitch(
            icon = painterResource(R.drawable.ic_dark_mode),
            label = stringResource(R.string.dark_mode_menu_switch),
            checked = uiState.isDarkModeEnabled,
            onCheckedChange = { menuViewModel.updateSettings(isDarkModeEnabled = it) }
        )
        Spacer(Modifier.height(32.dp))
        MenuButton(
            icon = painterResource(R.drawable.ic_info),
            label = stringResource(R.string.about_menu_button),
            onClick = { context.redirectToBrowser("https://challenge.braim.org/landing/app_contest") },
        )
        HorizontalDivider()
        MenuButton(
            icon = painterResource(R.drawable.ic_logout),
            label = stringResource(R.string.logout_menu_button),
            onClick = menuViewModel::quit,
        )
    }
}

@Composable
private fun MenuButton(
    icon: Painter,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun MenuSwitch(
    icon: Painter,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.requiredHeight(24.dp)
        )
    }
}

@Composable
private fun <T : Any> MenuDropDownTextField(
    icon: Painter,
    label: String,
    current: T,
    elements: List<T>,
    onElementSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(Modifier.weight(1f))
        DropdownTextField(
            current = current,
            elements = elements,
            onElementSelect = onElementSelected,
            modifier = Modifier.width(120.dp),
            colors = DropdownTextFieldDefaults.colors(
                borderColor = Color.Transparent
            ),
            contentPadding = PaddingValues()
        )
    }
}

@Preview
@Composable
private fun MenuButtonPreview() = AppTheme {
    MenuButton(
        icon = painterResource(R.drawable.ic_settings),
        label = stringResource(R.string.settings_menu_button),
        onClick = {}
    )
}

@Preview
@Composable
private fun MenuSwitchPreview() = AppTheme {
    MenuSwitch(
        icon = painterResource(R.drawable.ic_dark_mode),
        label = stringResource(R.string.dark_mode_menu_switch),
        checked = true,
        onCheckedChange = {}
    )
}

@Preview
@Composable
private fun MenuDropDownTextFieldPreview() = AppTheme {
    MenuDropDownTextField(
        icon = painterResource(R.drawable.ic_apartment),
        label = stringResource(R.string.city_menu_drop_down_text_field),
        current = "",
        elements = listOf(),
        onElementSelected = {}
    )
}