package aandrosov.city.app

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
        private const val FAVORITE_EVENTS_FORMAT_KEY = "FAVORITE_EVENTS_OF_%d"
        private const val FAVORITE_TICKETS_FORMAT_KEY = "FAVORITE_TICKETS_OF_%d"
    }

    var cityId: Long = 0
        private set

    var isDarkModeEnabled = false
        private set

    var favoriteNews = emptySet<Long>()
        private set

    var favoriteEvents = emptySet<Long>()
        private set

    var favoriteTickets = emptySet<Long>()
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
        favoriteNews: Set<Long> = this.favoriteNews,
        favoriteEvents: Set<Long> = this.favoriteEvents,
        favoriteTickets: Set<Long> = this.favoriteTickets
    ) {
        if (cityId != this.cityId) {
            editor.putLong(CITY_ID_KEY, cityId)
        }

        if (isDarkModeEnabled != this.isDarkModeEnabled) {
            editor.putBoolean(DARK_MODE_KEY, isDarkModeEnabled)
        }

        if (favoriteNews != this.favoriteNews) {
            val formattedKey = FAVORITE_NEWS_FORMAT_KEY.format(cityId)
            editor.putNumberSet(formattedKey, favoriteNews)
        }

        if (favoriteEvents != this.favoriteNews) {
            val formattedKey = FAVORITE_EVENTS_FORMAT_KEY.format(cityId)
            editor.putNumberSet(formattedKey, favoriteEvents)
        }

        if (favoriteTickets != this.favoriteTickets) {
            val formattedKey = FAVORITE_TICKETS_FORMAT_KEY.format(cityId)
            editor.putNumberSet(formattedKey, favoriteTickets)
        }

        editor.apply()
    }

    private fun onSharedPreferencesChanged(preferences: SharedPreferences) {
        cityId = preferences.getLong(CITY_ID_KEY, 0)
        isDarkModeEnabled = preferences.getBoolean(DARK_MODE_KEY, false)

        val formattedNewsKey = FAVORITE_NEWS_FORMAT_KEY.format(cityId)
        favoriteNews = preferences.getLongSet(formattedNewsKey, emptySet())!!

        val formattedEventsKey = FAVORITE_EVENTS_FORMAT_KEY.format(cityId)
        favoriteEvents = preferences.getLongSet(formattedEventsKey, emptySet())!!

        val formattedTicketsKey = FAVORITE_TICKETS_FORMAT_KEY.format(cityId)
        favoriteTickets = preferences.getLongSet(formattedTicketsKey, emptySet())!!

        listeners.forEach(OnSettingsChangeListener::onSettingsChanged)
    }

    private fun SharedPreferences.Editor.putNumberSet(key: String, values: Set<Number>?) {
        val converted = values?.map(Number::toString)?.toSet()
        putStringSet(key, converted)
    }

    private fun SharedPreferences.getLongSet(key: String, defValues: Set<Long>?): Set<Long>? {
        val stringSet = getStringSet(key, emptySet())!!

        val longSet = if (stringSet.isEmpty()) {
            defValues
        } else {
            stringSet.map(String::toLong).toSet()
        }

        return longSet
    }
}