package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.PlatformLocale
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

interface Platform {
    val name: String
    val is24Hours: Boolean
    fun getBestDateTimePattern(locale: PlatformLocale, skeleton: String): String
    fun getBatteryState(): Flow<BatteryState>
    fun getDataStorePreferences(): DataStorePreferences
    fun formatDate(date: LocalDate, style: Int, locale: PlatformLocale): String
    fun formatPercent(value: Double): String
}

//expect fun getPlatform(): Platform

@Composable
expect fun rememberPlatform(): Platform
