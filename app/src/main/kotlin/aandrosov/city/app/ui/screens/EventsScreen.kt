package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.redirectToBrowser
import aandrosov.city.app.ui.components.ArticleRenderer
import aandrosov.city.app.ui.components.DynamicAdvertisingBanners
import aandrosov.city.app.ui.components.EmptyView
import aandrosov.city.app.ui.components.FavoriteButton
import aandrosov.city.app.ui.components.FavoriteButtonDefaults
import aandrosov.city.app.ui.components.FloatingPrice
import aandrosov.city.app.ui.components.RowSelector
import aandrosov.city.app.ui.states.CategoryState
import aandrosov.city.app.ui.states.localizedTitle
import aandrosov.city.app.ui.themes.AppTheme
import aandrosov.city.app.ui.viewModels.EventsViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    modifier: Modifier = Modifier,
    eventsViewModel: EventsViewModel = koinViewModel()
) {
    val uiState by eventsViewModel.uiState.collectAsState()
    val categories = uiState.categories.toMutableList().also {
        it.add(0, CategoryState.ALL)
        it.add(1, CategoryState.FAVORITE)
    }

    val bottomSheetState = rememberModalBottomSheetState(true)
    var showBottomSheet by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { eventsViewModel.fetchEvents(uiState.category) },
        modifier = modifier.padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RowSelector(
                current = uiState.category.localizedTitle,
                items = categories.map { it.localizedTitle },
                onItemSelect = {
                    val category = categories[it]
                    eventsViewModel.fetchEvents(category)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
            DynamicAdvertisingBanners()
            uiState.categories.forEach { category ->
                val events = uiState.events.filter { it.categoryId == category.id }
                if (events.isNotEmpty()) {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(events) { event ->
                            EventCard(
                                title = event.title,
                                thumbnail = rememberAsyncImagePainter(
                                    event.imageUrl,
                                    onError = {
                                        println(it.result.throwable)
                                    }
                                ),
                                price = event.price,
                                date = event.date,
                                onClick = {
                                    eventsViewModel.fetchEventContent(event.id, event.providerUrl)
                                    showBottomSheet = true
                                },
                                isFavorite = event.isFavorite,
                                onFavoriteClick = { eventsViewModel.toggleFavorite(event.id) }
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }
        }
        if (!uiState.isLoading && uiState.events.isEmpty()) {
            EmptyView()
        }
    }

    Box(
        modifier = modifier.padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                val content = uiState.eventContent
                Box(contentAlignment = Alignment.Center) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                    ) {
                        ArticleRenderer(
                            blocks = content.content,
                            modifier = Modifier.weight(1f)
                        )
                        val context = LocalContext.current
                        BuyTicketsButton(
                            onClick = { context.redirectToBrowser(content.providerUrl) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading
                        )
                    }
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(
    title: String,
    thumbnail: Painter,
    price: String,
    date: String,
    onClick: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .width(IntrinsicSize.Min)
    ) {
        Box {
            Image(
                painter = thumbnail,
                contentDescription = price,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .widthIn(max = 268.dp)
            )
            FloatingPrice(
                price = price,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            )
            FavoriteButton(
                isFavorite = isFavorite,
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                colors = FavoriteButtonDefaults.colors(
                    inactiveColor = Color.White
                )
            )
        }
        EventCardTitle(
            title = title,
            modifier = Modifier.fillMaxWidth()
        )
        EventCardDate(date)
    }
}

@Composable
private fun BuyTicketsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.small
    ) {
        Text(stringResource(R.string.buy_event_button))
    }
}

@Composable
private fun EventCardTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun EventCardDate(
    date: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = date,
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondary,
        style = MaterialTheme.typography.labelLarge
    )
}

@Preview
@Composable
private fun EventCardPreview() = AppTheme {
    EventCard(
        title = "Мамма мимо! или Мюзикл пошёл не по плану",
        thumbnail = painterResource(R.drawable.img_event_card_960_690),
        price = "от 400₽",
        date = "14 апреля",
        onClick = {},
        isFavorite = false,
        onFavoriteClick = {}
    )
}