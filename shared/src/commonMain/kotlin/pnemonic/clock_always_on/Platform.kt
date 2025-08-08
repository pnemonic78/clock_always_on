package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow
import java.util.Locale

interface Platform {
    val name: String
    val is24Hours: Boolean
    fun getBestDateTimePattern(locale: Locale, skeleton: String): String
    fun getBatteryState(): Flow<BatteryState>
}

//expect fun getPlatform(): Platform

@Composable
expect fun rememberPlatform(): Platform
