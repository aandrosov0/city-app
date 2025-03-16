package aandrosov.city.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        actionBar?.hide()
        adaptToTheme()

        super.onCreate(savedInstanceState)
        setContent {
            Spacer(
                Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .size(48.dp)
                    .background(Color.Red)
            )
            Text(
                text = "androidx.core:core-ktx:1.15.0",
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(top = 64.dp)
            )
        }
    }
}

fun Activity.adaptToTheme(dark: Boolean = false) {
    WindowCompat
        .getInsetsController(window, window.decorView)
        .isAppearanceLightStatusBars = !dark
}

@SuppressLint("DiscouragedApi")
fun Context.getStringResourceByName(name: String): String {
    val identifier = resources.getIdentifier(name, "string", packageName)
    return resources.getString(identifier)
}