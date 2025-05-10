package aandrosov.city.app.ui.components

import aandrosov.city.app.R
import aandrosov.city.app.ui.themes.AppTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun AdvertisingBanner(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.img_advertising_banner),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillWidth,
    )
}

@Composable
fun DynamicAdvertisingBanners(modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState { 3 }

    LaunchedEffect(Unit) {
        var nextPage = 0
        while (true) {
            delay(3.seconds)
            if (nextPage >= pagerState.pageCount - 1) {
                nextPage = 0
            } else {
                nextPage++
            }
            pagerState.animateScrollToPage(nextPage)
        }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        pageSize = PageSize.Fixed(320.dp),
        pageSpacing = 14.dp
    ) {
        AdvertisingBanner(
            Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun DynamicAdvertisingBannersPreview() = AppTheme {
    DynamicAdvertisingBanners()
}