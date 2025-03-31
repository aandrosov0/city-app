package aandrosov.city.app.ui.components

import aandrosov.city.app.R
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun AdvertisingBanner(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.img_advertising_banner),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillWidth,
    )
}