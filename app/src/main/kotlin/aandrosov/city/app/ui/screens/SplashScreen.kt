package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.ui.navigation.Onboarding
import aandrosov.city.app.ui.themes.AppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
internal fun SplashScreen(
    onNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(5.seconds)
        onNavigate(Onboarding)
    }

    Box(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AppLogo(Modifier.align(Alignment.Center))
        Image(
            painter = painterResource(R.drawable.splash_background),
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun AppLogo(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_apartment),
            contentDescription = stringResource(R.string.app_name),
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() = AppTheme {
    SplashScreen(onNavigate = {})
}