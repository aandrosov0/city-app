package aandrosov.city.app

import aandrosov.city.app.ui.App
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        actionBar?.hide()
        adaptToTheme()

        super.onCreate(savedInstanceState)
        setContent { App() }
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