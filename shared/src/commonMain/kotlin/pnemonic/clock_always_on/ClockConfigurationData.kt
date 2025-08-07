package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClockConfigurationData(
    private val dataStore: DataStorePreferences
) {
    fun configuration(): Flow<ClockConfiguration> {
        val config = ClockConfiguration()
        return dataStore.data.map { p ->
            config.copy(
                isDigital = p[booleanPreferencesKey(KEY_DIGITAL)] ?: config.isDigital,
                is24Hours = p[booleanPreferencesKey(KEY_24)] ?: config.is24Hours,
                isSeconds = p[booleanPreferencesKey(KEY_SECONDS)] ?: config.isSeconds,
                isDate = p[booleanPreferencesKey(KEY_DATE)] ?: config.isDate,
                isBattery = p[booleanPreferencesKey(KEY_BATTERY)] ?: config.isBattery,
                isBounce = p[booleanPreferencesKey(KEY_BOUNCE)] ?: config.isBounce,
                backgroundColor = Color(
                    p[longPreferencesKey(KEY_BACKGROUND_COLOR)]?.toULong()
                        ?: config.backgroundColor.value
                ),
                textColor = Color(
                    p[longPreferencesKey(KEY_TEXT_COLOR)]?.toULong() ?: config.textColor.value
                ),
            )
        }
    }

    suspend fun set24Hours(value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(KEY_24)] = value
        }
    }

    suspend fun setSeconds(value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(KEY_SECONDS)] = value
        }
    }

    suspend fun setDate(value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(KEY_DATE)] = value
        }
    }

    suspend fun setBattery(value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(KEY_BATTERY)] = value
        }
    }

    suspend fun setBounce(value: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(KEY_BOUNCE)] = value
        }
    }

    suspend fun setTextColor(value: Color) {
        dataStore.edit {
            it[longPreferencesKey(KEY_TEXT_COLOR)] = value.value.toLong()
        }
    }

    suspend fun setBackgroundColor(value: Color) {
        dataStore.edit {
            it[longPreferencesKey(KEY_BACKGROUND_COLOR)] = value.value.toLong()
        }
    }

    companion object {
        private const val KEY_DIGITAL = "digital"
        private const val KEY_24 = "24"
        private const val KEY_SECONDS = "seconds"
        private const val KEY_DATE = "date"
        private const val KEY_BATTERY = "battery"
        private const val KEY_BOUNCE = "bounce"
        private const val KEY_BACKGROUND_COLOR = "backgroundColor"
        private const val KEY_TEXT_COLOR = "textColor"
    }
}

@Composable
fun rememberClockConfigurationData(dataStore: DataStorePreferences): ClockConfigurationData {
    return remember { ClockConfigurationData(dataStore) }
}