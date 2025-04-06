package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.ui.components.BackTopBar
import aandrosov.city.app.ui.states.localizedName
import aandrosov.city.app.ui.viewModels.WeatherViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun WeatherScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) { weatherViewModel.fetchForecast() }
    val uiState by weatherViewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = { BackTopBar(onBackClick = onNavigateUp) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = stringResource(
                            R.string.weather_screen_temperature_headline,
                            uiState.weather.currentTemperature
                        ),
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(Modifier.height(40.dp))
                    Text(
                        text = uiState.weather.currentWeatherCode.localizedName,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
        }
    }
}