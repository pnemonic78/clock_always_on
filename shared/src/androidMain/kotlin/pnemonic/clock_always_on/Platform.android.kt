package pnemonic.clock_always_on

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.PlatformLocale
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import java.text.NumberFormat
import kotlin.time.ExperimentalTime

private const val delayBattery = DateUtils.SECOND_IN_MILLIS * 5

class AndroidPlatform(private val context: Context) : Platform {
    private val dateFormatters = mutableMapOf<String, java.text.DateFormat>()

    private val percentFormatter = NumberFormat.getPercentInstance()

    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    override val is24Hours: Boolean get() = android.text.format.DateFormat.is24HourFormat(context)

    override fun getBestDateTimePattern(locale: PlatformLocale, skeleton: String): String {
        return android.text.format.DateFormat.getBestDateTimePattern(locale, skeleton)
    }

    override fun getBatteryState(): Flow<BatteryState> = flow {
        while (true) {
            val state = BatteryUtils.getState(context) ?: break
            emit(state)
            delay(delayBattery)
        }
    }

    override fun getDataStorePreferences(): DataStorePreferences {
        var ds = dataStore
        if (ds == null) {
            ds = createDataStore(context)
            dataStore = ds
        }
        return ds
    }

    @OptIn(ExperimentalTime::class)
    override fun formatDate(date: LocalDate, style: Int, locale: PlatformLocale): String {
        val key = "$style/$locale"
        var formatter = dateFormatters[key]
        if (formatter == null) {
            formatter = java.text.DateFormat.getDateInstance(style, locale)
            dateFormatters[key] = formatter
        }
        val millis = date.toEpochDays() * 86_400_000L
        return formatter.format(millis)
    }

    override fun formatPercent(value: Double): String {
        return percentFormatter.format(value)
    }

    companion object {
        private var dataStore: DataStorePreferences? = null
    }
}

@Composable
actual fun rememberPlatform(): Platform {
    val context = LocalContext.current
    return remember { AndroidPlatform(context) }
}