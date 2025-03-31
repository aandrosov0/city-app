package aandrosov.city.app.ui.screens

import aandrosov.city.app.ui.components.ArticleRenderer
import aandrosov.city.app.ui.components.BackTopBar
import aandrosov.city.app.ui.viewModels.ArticleViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            ArticleRenderer(
                blocks = blocks,
                modifier = Modifier.fillMaxSize()
            )
            if (uiState.isLoading) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }
}
