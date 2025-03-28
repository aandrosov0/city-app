package aandrosov.city.app.ui.screens

import aandrosov.city.app.R
import aandrosov.city.app.ui.components.BackTopBar
import aandrosov.city.app.ui.states.ArticleHeadlineState
import aandrosov.city.app.ui.states.ArticleParagraphState
import aandrosov.city.app.ui.states.ArticleSubheadlineState
import aandrosov.city.app.ui.states.ArticleThumbnailState
import aandrosov.city.app.ui.themes.AppTheme
import aandrosov.city.app.ui.viewModels.ArticleViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ArticleScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    articleViewModel: ArticleViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) { articleViewModel.fetch() }
    val uiState by articleViewModel.uiState.collectAsState()
    val blocks = uiState.article.blocks

    Scaffold(
        topBar = { BackTopBar(onBackClick = onNavigateBack) },
        modifier = modifier
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 16.dp)
            ) {
                items(blocks) { block ->
                    when (block) {
                        is ArticleHeadlineState -> Headline(block.text)
                        is ArticleSubheadlineState -> Subheadline(block.text)
                        is ArticleParagraphState -> Paragraph(block.text)
                        is ArticleThumbnailState -> Thumbnail(
                            painter = rememberAsyncImagePainter(block.url),
                            caption = block.caption.ifEmpty { null }
                        )
                    }
                }
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun Headline(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = MaterialTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium
    )
}

@Composable
private fun Subheadline(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.padding(top = 8.dp),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
private fun Paragraph(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = AnnotatedString.fromHtml(text),
        modifier = modifier.padding(top = 16.dp),
        textAlign = TextAlign.Justify,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
private fun Thumbnail(
    painter: Painter,
    modifier: Modifier = Modifier,
    caption: String? = null
) {
    Column(modifier) {
        Image(
            painter = painter,
            contentDescription = caption ?: "",
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.FillWidth
        )
        if (caption != null) {
            Caption(text = caption)
        }
    }
}

@Composable
private fun Caption(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        color = Color.Gray,
        textAlign = TextAlign.Justify,
        style = MaterialTheme.typography.labelMedium
    )
}

@Preview
@Composable
private fun HeadlinePreview() = AppTheme {
    Headline("Headline")
}

@Preview
@Composable
private fun SubheadlinePreview() = AppTheme {
    Subheadline("Subheadline")
}

@Preview
@Composable
private fun ParagraphPreview() = AppTheme {
    val text = """
        Non aut omnis sunt perferendis sit. Distinctio possimus nihil nihil exercitationem sit ratione. 
        Sapiente voluptas ea sit minus. Dolores molestias corporis aut adipisci blanditiis
    """.trimIndent()
    Paragraph(text)
}

@Preview
@Composable
private fun ThumbnailPreview() = AppTheme {
    Thumbnail(
        painter = painterResource(R.drawable.img_article_thumbnail),
        caption = "This is a caption"
    )
}

@Preview
@Composable
private fun CaptionPreview() = AppTheme {
    Caption("Caption")
}