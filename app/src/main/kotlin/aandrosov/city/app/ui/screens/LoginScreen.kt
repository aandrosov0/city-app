package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.ui.components.DropdownTextField
import aandrosov.city.app.ui.states.CityState
import aandrosov.city.app.ui.viewModels.LoginViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) { loginViewModel.fetch() }
    val uiState by loginViewModel.uiState.collectAsState()
    var selectedCity by remember { mutableStateOf<CityState?>(null) }
    LaunchedEffect(uiState.cities) { selectedCity = uiState.cities.firstOrNull() }

    Scaffold { innerPadding ->
        Box {
            Column(
                modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp)
                    .padding(innerPadding)
            ) {
                Text(
                    text = stringResource(R.string.login_screen_headline),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(32.dp))
                Text(
                    text = stringResource(R.string.login_screen_subheadline),
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(Modifier.height(70.dp))
                DropdownTextField(
                    current = selectedCity ?: CityState(),
                    elements = uiState.cities,
                    onElementSelect = { selectedCity = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.weight(1f))
                NextButton(
                    onClick = { loginViewModel.selectCity(selectedCity!!.id) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading && selectedCity != null
                )
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun NextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.small
    ) {
        Text(stringResource(R.string.im_here))
    }
}