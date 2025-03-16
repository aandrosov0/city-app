package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.ui.themes.AppTheme
import aandrosov.city.app.ui.viewModels.NewsViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
internal fun NewsScreen(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) { newsViewModel.fetch() }
    val uiState by newsViewModel.uiState.collectAsState()

//    Scaffold(modifier) { innerPadding ->
//        Box(Modifier.padding(innerPadding)) {
//            Column(Modifier.fillMaxSize()) {
//                LazyColumn(Modifier.fillMaxSize()) {
//                    items(uiState.news) { news ->
//                        NewsCard(
//                            image = rememberAsyncImagePainter(news.imageUrl),
//                            title = news.title,
//                            category = "",
//                            publicationDate = news.publishedAt,
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                }
//            }
//            if (uiState.isLoading) {
//                CircularProgressIndicator(Modifier.align(Alignment.Center))
//            }
//        }
//    }
}

@Composable
private fun NewsCard(
    image: Painter,
    title: String,
    category: String,
    publicationDate: LocalDateTime,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Max),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(126.dp)
                .clip(MaterialTheme.shapes.small)
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
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_time),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = formatDateTime(publicationDate),
                    style = MaterialTheme.typography.labelMedium
                )
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
        publicationDate = LocalDateTime.now()
    )
}

private fun formatDateTime(dateTime: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMMM uuuu")
    return dateTime.format(formatter)
}