package aandrosov.city.app

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.redirectToBrowser(url: String) {
    val urlIntent = Intent(
        Intent.ACTION_VIEW,
        url.toUri()
    )
    startActivity(urlIntent)
}