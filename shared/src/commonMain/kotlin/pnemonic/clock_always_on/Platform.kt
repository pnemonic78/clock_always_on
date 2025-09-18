package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.intl.PlatformLocale
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

interface Platform {
    val name: String
    val is24Hours: Boolean
    fun getBatteryState(): Flow<BatteryState>
    fun getDataStorePreferences(): DataStorePreferences
    fun formatDate(date: LocalDate, style: Int, locale: PlatformLocale): String
    fun formatPercent(value: Double): String
    fun formatTime(time: LocalDateTime, style: TimeFormat, locale: PlatformLocale): String
}

//expect fun getPlatform(): Platform

@Composable
expect fun rememberPlatform(): Platform

val LocalPlatform = compositionLocalOf<Platform> {
    object : Platform {
        override val name: String = ""

        override val is24Hours: Boolean = false

        override fun getBatteryState(): Flow<BatteryState> = emptyFlow()

        override fun getDataStorePreferences(): DataStorePreferences {
            TODO("Not yet implemented")
        }

        override fun formatDate(
            date: LocalDate,
            style: Int,
            locale: PlatformLocale
        ): String = ""

        override fun formatPercent(value: Double): String = ""

        override fun formatTime(
            time: LocalDateTime,
            style: TimeFormat,
            locale: PlatformLocale
        ): String = ""
    }
}
