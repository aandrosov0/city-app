package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.ui.components.AdvertisingBanner
import aandrosov.city.app.ui.components.EmptyView
import aandrosov.city.app.ui.components.FavoriteButton
import aandrosov.city.app.ui.components.RowSelector
import aandrosov.city.app.ui.navigation.Article
import aandrosov.city.app.ui.shimmer
import aandrosov.city.app.ui.states.CategoryState
import aandrosov.city.app.ui.states.localizedTitle
import aandrosov.city.app.ui.themes.AppTheme
import aandrosov.city.app.ui.viewModels.NewsViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewsScreen(
    onNavigate: (Any) -> Unit,
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel = koinViewModel()
) {
    val uiState by newsViewModel.uiState.collectAsState()

    val categories = uiState.categories.toMutableList().also {
        it.add(0, CategoryState.ALL)
        it.add(1, CategoryState.FAVORITE)
    }

    var searchableText by remember { mutableStateOf("") }

    var news = uiState.news
    LaunchedEffect(searchableText) {
        newsViewModel.fetchNews(searchableText = searchableText)
    }

    Scaffold(modifier) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { newsViewModel.fetchNews(uiState.category) },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp)
        ) {
            Column(Modifier.fillMaxSize()) {
                SearchTextField(
                    value = searchableText,
                    onValueChange = { searchableText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp, bottom = 8.dp)
                )
                RowSelector(
                    current = uiState.category.localizedTitle,
                    items = categories.map { it.localizedTitle },
                    onItemSelect = {
                        val category = categories[it]
                        newsViewModel.fetchNews(category)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    itemsIndexed(news) { index, news ->
                        if (index % 5 == 0) {
                            AdvertisingBanner(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            )
                        }
                        NewsCard(
                            image = rememberAsyncImagePainter(news.imageUrl),
                            title = news.title,
                            category = uiState.categories.find { it.id == news.categoryId }!!.localizedTitle,
                            publicationDate = news.publishedAt.toInstant(),
                            isFavorite = news.isFavorite,
                            onClick = { onNavigate(Article(news.id)) },
                            onFavoriteClick = { newsViewModel.toggleFavorite(news.id) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (uiState.isLoading && uiState.news.isEmpty()) {
                        items(6) {
                            NewsCardPlaceholder()
                        }
                    }
                }
            }
            if (!uiState.isLoading && uiState.news.isEmpty()) {
                EmptyView()
            }
        }
    }
}

@Composable
private fun NewsCard(
    image: Painter,
    title: String,
    category: String,
    publicationDate: Instant,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .size(126.dp)
                .clip(MaterialTheme.shapes.small)
                .shimmer()
        )
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = category,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.weight(1f))
            Row(verticalAlignment = Alignment.Bottom) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_time),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = publicationDate.formatDateTime(),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Spacer(Modifier.weight(1f))
                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
fun NewsCardPlaceholder(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Spacer(
            Modifier
                .size(126.dp)
                .clip(MaterialTheme.shapes.small)
                .shimmer()
        )
        val spacing = 6.dp
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            repeat(3) {
                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .shimmer()
                        .padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = MaterialTheme.colorScheme.secondary
    val containerShape = MaterialTheme.shapes.small
    val contentColor = MaterialTheme.colorScheme.onSecondary
    val textStyle = TextStyle.Default.copy(
        fontWeight = FontWeight.W700,
        color = contentColor
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .height(IntrinsicSize.Max)
            .width(240.dp),
        singleLine = true,
        textStyle = textStyle,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
    ) { innerTextField ->
        Box(
            modifier = Modifier
                .background(containerColor, containerShape)
                .padding(8.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                if (value.isBlank()) {
                    Text(
                        text = stringResource(R.string.search_text_field_placeholder),
                        style = textStyle
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f)) {
                        innerTextField()
                    }
                    Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NewsCardPreview() = AppTheme {
    NewsCard(
        image = painterResource(R.drawable.img_news_sample),
        title = "Почти 1 000 домов в Воронеже обезвожены из-за коммунальных работ",
        category = "Я - репортёр",
        publicationDate = Instant.now(),
        isFavorite = false,
        onClick = {},
        onFavoriteClick = {}
    )
}

@Preview
@Composable
private fun NewsCardPlaceholderPreview() = AppTheme {
    NewsCardPlaceholder()
}

@Preview
@Composable
private fun SearchTextFieldPreview() = AppTheme {
    SearchTextField(
        value = "",
        onValueChange = {}
    )
}

private fun Instant.formatDateTime(): String {
    return DateTimeFormatter
        .ofPattern("dd MMMM uuuu")
        .withZone(ZoneId.systemDefault())
        .format(this)
}