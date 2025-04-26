package aandrosov.city.app.ui

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener

class AppSettings(preferences: SharedPreferences) {
    fun interface OnSettingsChangeListener {
        fun onSettingsChanged()
    }

    companion object {
        private const val CITY_ID_KEY = "CITY_ID"
        private const val DARK_MODE_KEY = "DARK_MODE"
        private const val FAVORITE_NEWS_FORMAT_KEY = "FAVORITE_NEWS_OF_%d"
    }

    var cityId: Long = 0
        private set

    var isDarkModeEnabled = false
        private set

    var favoriteNews = emptySet<Long>()
        private set

    private val editor = preferences.edit()

    private val listeners = mutableListOf<OnSettingsChangeListener>()

    private val onSharedPreferencesChangeListener = OnSharedPreferenceChangeListener { pref, _ ->
        onSharedPreferencesChanged(pref)
    }

    init {
        preferences.registerOnSharedPreferenceChangeListener(onSharedPreferencesChangeListener)
        onSharedPreferencesChangeListener.onSharedPreferenceChanged(preferences, null)
    }

    fun registerOnSettingsChangeListener(listener: OnSettingsChangeListener) {
        listener.onSettingsChanged()
        listeners.add(listener)
    }

    fun unregisterOnSettingsChangeListener(listener: OnSettingsChangeListener) {
        listeners.remove(listener)
    }

    fun update(
        cityId: Long = this.cityId,
        isDarkModeEnabled: Boolean = this.isDarkModeEnabled,
        favoriteNews: Set<Long> = this.favoriteNews
    ) {
        if (cityId != this.cityId) {
            editor.putLong(CITY_ID_KEY, cityId)
        }

        if (isDarkModeEnabled != this.isDarkModeEnabled) {
            editor.putBoolean(DARK_MODE_KEY, isDarkModeEnabled)
        }

        if (favoriteNews != this.favoriteNews) {
            val converted = favoriteNews.map(Long::toString).toSet()
            val formattedKey = FAVORITE_NEWS_FORMAT_KEY.format(cityId)
            editor.putStringSet(formattedKey, converted)
        }

        editor.apply()
    }

    private fun onSharedPreferencesChanged(preferences: SharedPreferences) {
        cityId = preferences.getLong(CITY_ID_KEY, 0)
        isDarkModeEnabled = preferences.getBoolean(DARK_MODE_KEY, false)

        val formattedNewsKey = FAVORITE_NEWS_FORMAT_KEY.format(cityId)
        val newsIds = preferences.getStringSet(formattedNewsKey, emptySet())!!
        favoriteNews = newsIds.map(String::toLong).toSet()

        listeners.forEach(OnSettingsChangeListener::onSettingsChanged)
    }
}