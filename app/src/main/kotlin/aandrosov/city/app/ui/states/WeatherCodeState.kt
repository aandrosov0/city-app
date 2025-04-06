package aandrosov.city.app.ui.states

import aandrosov.city.app.R
import aandrosov.city.data.models.WeatherCodeModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

enum class WeatherCodeState {
    CLEAR_SKY,
    MAINLY_CLEAR,
    PARTLY_CLOUDY,
    OVERCAST,
    FOG,
    DRIZZLE,
    RAIN,
    SNOW_FALL,
    SNOW_GRAINS,
    THUNDERSTORM,
}

val WeatherCodeState.localizedName
    @Composable get() = when (this) {
        WeatherCodeState.CLEAR_SKY -> stringResource(R.string.weather_code_clear_sky)
        WeatherCodeState.MAINLY_CLEAR -> stringResource(R.string.weather_code_mainly_clear)
        WeatherCodeState.PARTLY_CLOUDY -> stringResource(R.string.weather_code_partly_cloudy)
        WeatherCodeState.OVERCAST -> stringResource(R.string.weather_code_overcast)
        WeatherCodeState.FOG -> stringResource(R.string.weather_code_fog)
        WeatherCodeState.DRIZZLE -> stringResource(R.string.weather_code_drizzle)
        WeatherCodeState.RAIN -> stringResource(R.string.weather_code_rain)
        WeatherCodeState.SNOW_FALL -> stringResource(R.string.weather_code_snow_fall)
        WeatherCodeState.SNOW_GRAINS -> stringResource(R.string.weather_code_snow_grains)
        WeatherCodeState.THUNDERSTORM -> stringResource(R.string.weather_code_thunderstorm)
    }

fun WeatherCodeModel.toState() = when (this) {
    WeatherCodeModel.CLEAR_SKY -> WeatherCodeState.CLEAR_SKY
    WeatherCodeModel.MAINLY_CLEAR -> WeatherCodeState.MAINLY_CLEAR
    WeatherCodeModel.PARTLY_CLOUDY -> WeatherCodeState.PARTLY_CLOUDY
    WeatherCodeModel.OVERCAST -> WeatherCodeState.OVERCAST
    WeatherCodeModel.FOG, WeatherCodeModel.DEPOSITING_RIME_FOG -> WeatherCodeState.FOG
    WeatherCodeModel.DRIZZLE_LIGHT,
    WeatherCodeModel.DRIZZLE_MODERATE,
    WeatherCodeModel.DRIZZLE_DENSE_INTENSITY,
    WeatherCodeModel.FREEZING_DRIZZLE_LIGHT,
    WeatherCodeModel.FREEZING_DRIZZLE_DENSE_INTENSITY -> WeatherCodeState.DRIZZLE
    WeatherCodeModel.RAIN_SLIGHTLY,
    WeatherCodeModel.RAIN_MODERATE,
    WeatherCodeModel.FREEZING_RAIN_LIGHT,
    WeatherCodeModel.FREEZING_RAIN_HEAVY_INTENSITY,
    WeatherCodeModel.RAIN_SHOWERS_LIGHT,
    WeatherCodeModel.RAIN_SHOWERS_MODERATE,
    WeatherCodeModel.RAIN_SHOWERS_VIOLENT,
    WeatherCodeModel.RAIN_HEAVY_INTENSITY -> WeatherCodeState.RAIN
    WeatherCodeModel.SNOW_FALL_LIGHT,
    WeatherCodeModel.SNOW_FALL_MODERATE,
    WeatherCodeModel.SNOW_FALL_HEAVY_INTENSITY,
    WeatherCodeModel.SNOW_SHOWERS_HEAVY,
    WeatherCodeModel.SNOW_SHOWERS_SLIGHT -> WeatherCodeState.SNOW_FALL
    WeatherCodeModel.SNOW_GRAINS -> WeatherCodeState.SNOW_GRAINS
    WeatherCodeModel.THUNDERSTORM,
    WeatherCodeModel.THUNDERSTORM_SLIGHTLY,
    WeatherCodeModel.THUNDERSTORM_HEAVY_HAIL -> WeatherCodeState.THUNDERSTORM
}