package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.ui.components.AppLogo
import aandrosov.city.app.ui.navigation.Events
import aandrosov.city.app.ui.navigation.HomeNavigation
import aandrosov.city.app.ui.navigation.Menu
import aandrosov.city.app.ui.navigation.News
import aandrosov.city.app.ui.navigation.Tickets
import aandrosov.city.app.ui.themes.AppTheme
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

data class TopLevelRoute(
    val route: Any,
    @StringRes val label: Int,
    @DrawableRes val icon: Int
)

@Composable
fun HomeScreen(
    onRootNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = modifier,
        topBar = { HomeTopBar(onNotificationsClick = {}) },
        bottomBar = {
            HomeNavigationBar(
                current = navBackStackEntry?.destination ?: NavDestination(""),
                routes = listOf(
                    TopLevelRoute(News, R.string.news, R.drawable.ic_news),
                    TopLevelRoute(Events, R.string.events, R.drawable.ic_tickets),
                    TopLevelRoute(Tickets, R.string.tickets, R.drawable.ic_credit_card),
                    TopLevelRoute(Menu, R.string.menu, R.drawable.ic_menu),
                ),
                onRouteSelect = {
                    navController.navigate(it.route) {
                        navController.popBackStack()
                    }
                }
            )
        }
    ) { innerPadding ->
        HomeNavigation(
            navController = navController,
            onRootNavigate = onRootNavigate,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        )
    }
}

@Composable
private fun HomeTopBar(
    onNotificationsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppLogo(Modifier.padding(start = 4.dp))
        NotificationsButton(onNotificationsClick)
    }
}

@Composable
private fun NotificationsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_notifications),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun HomeNavigationBar(
    current: NavDestination,
    routes: List<TopLevelRoute>,
    onRouteSelect: (TopLevelRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .shadow(8.dp)
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp)
            .fillMaxWidth()
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        routes.forEach { route ->
            val selected = current.hierarchy.any { it.hasRoute(route.route::class) }
            BottomNavigationItem(
                icon = painterResource(route.icon),
                label = stringResource(route.label),
                selected = selected,
                onClick = { onRouteSelect(route) }
            )
        }
    }
}

@Composable
private fun BottomNavigationItem(
    icon: Painter,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }

    Column(
        modifier = modifier
            .padding(4.dp)
            .clickable(
                interactionSource = null,
                indication = ripple(),
                onClick = onClick,
                enabled = !selected
            ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompositionLocalProvider(LocalContentColor provides color) {
            Icon(
                painter = icon,
                contentDescription = null,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview
@Composable
private fun HomeTopBarPreview() = AppTheme {
    HomeTopBar(onNotificationsClick = {})
}

@Preview
@Composable
private fun HomeNavigationBarPreview() = AppTheme {
    HomeNavigationBar(
        current = NavDestination(""),
        routes = listOf(
            TopLevelRoute(Unit, R.string.news, R.drawable.ic_news),
            TopLevelRoute(Unit, R.string.events, R.drawable.ic_tickets),
            TopLevelRoute(Unit, R.string.tickets, R.drawable.ic_credit_card),
            TopLevelRoute(Unit, R.string.menu, R.drawable.ic_menu),
        ),
        onRouteSelect = {}
    )
}
