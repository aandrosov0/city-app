package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.redirectToBrowser
import aandrosov.city.app.ui.components.ArticleRenderer
import aandrosov.city.app.ui.components.FloatingPrice
import aandrosov.city.app.ui.themes.AppTheme
import aandrosov.city.app.ui.viewModels.TicketsViewModel
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
internal fun TicketsScreen(
    modifier: Modifier = Modifier,
    ticketsViewModel: TicketsViewModel = koinViewModel()
) {
    val uiState by ticketsViewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { ticketsViewModel.fetchTickets() }

    val bottomSheetState = rememberModalBottomSheetState(true)
    var showBottomSheet by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { ticketsViewModel.fetchTickets() },
        modifier = modifier.padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            uiState.categories.forEach { category ->
                val events = uiState.tickets.filter { it.categoryId == category.id }
                if (events.isNotEmpty()) {
                    Text(
                        text = category.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(events) { ticket ->
                            TicketCard(
                                title = ticket.title,
                                thumbnail = rememberAsyncImagePainter(ticket.imageUrl),
                                price = ticket.price,
                                onClick = {
                                    ticketsViewModel.fetchTicketContent(
                                        ticket.id,
                                        ticket.providerUrl
                                    )
                                    showBottomSheet = true
                                }
                            )
                        }
                    }
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                val content = uiState.ticketContent
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
private fun BuyTicketsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.small
    ) {
        Text(stringResource(R.string.buy_tickets_button))
    }
}

@Composable
private fun TicketCard(
    title: String,
    thumbnail: Painter,
    price: String,
    onClick: () -> Unit,
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
                contentDescription = title,
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
        }
        TicketCardTitle(title = title)
    }
}

@Composable
private fun TicketCardTitle(
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

@Preview
@Composable
private fun TicketCardPreview() = AppTheme {
    TicketCard(
        title = "SPA-программы в салоне Sahar&Vosk",
        thumbnail = painterResource(R.drawable.img_event_card_960_690),
        price = "800р",
        onClick = {}
    )
}