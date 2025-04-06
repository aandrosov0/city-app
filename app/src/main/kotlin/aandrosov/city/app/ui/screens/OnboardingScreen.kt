package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.getStringResourceByName
import aandrosov.city.app.ui.navigation.Login
import aandrosov.city.app.ui.themes.AppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private enum class OnboardingPage {
    ONBOARDING_1,
    ONBOARDING_2,
    ONBOARDING_3
}

private val OnboardingPage.painter
    @Composable get() = when (this) {
        OnboardingPage.ONBOARDING_1 -> painterResource(R.drawable.onboarding_1)
        OnboardingPage.ONBOARDING_2 -> painterResource(R.drawable.onboarding_2)
        OnboardingPage.ONBOARDING_3 -> painterResource(R.drawable.onboarding_3)
    }

private val OnboardingPage.title
    @Composable get() = LocalContext
        .current
        .getStringResourceByName("${name.lowercase()}_title")

private val OnboardingPage.body
    @Composable get() = LocalContext
        .current
        .getStringResourceByName("${name.lowercase()}_body")

@Composable
internal fun OnboardingScreen(
    onNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = OnboardingPage.entries
    var currentPageIndex by remember { mutableIntStateOf(0) }
    val currentPage = pages[currentPageIndex]

    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = currentPage.painter,
            contentDescription = stringResource(R.string.onboarding),
            modifier = Modifier
                .fillMaxWidth()
                .weight(.69f)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.verticalGradient(
                            colors = listOf(Color.White, Color.Transparent),
                            endY = size.height * .08f
                        )
                    )
                },
            contentScale = ContentScale.FillBounds
        )
        Column(
            Modifier
                .weight(.31f)
                .padding(24.dp)
        ) {
            Text(
                text = currentPage.title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = currentPage.body,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PageIndication(
                    pages = pages.size,
                    activeIndex = currentPageIndex
                )
                NextButton(
                    onClick = {
                        if (currentPageIndex + 1 > pages.lastIndex) {
                            onNavigate(Login)
                        } else {
                            currentPageIndex++
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(stringResource(R.string.next))
    }
}

@Composable
private fun PageIndication(
    pages: Int,
    activeIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        for (i in 0..<pages) {
            val color = if (i == activeIndex) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            }
            val shape = RoundedCornerShape(100)

            Spacer(
                Modifier
                    .size(14.dp)
                    .background(color, shape)
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() = AppTheme {
    OnboardingScreen(onNavigate = {})
}