package pnemonic.clock_always_on

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.PlatformLocale
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import java.text.NumberFormat
import java.text.SimpleDateFormat
import kotlin.time.ExperimentalTime

private const val delayBattery = DateUtils.SECOND_IN_MILLIS * 10

class AndroidPlatform(private val context: Context) : Platform {
    private val dateFormatters = mutableMapOf<String, java.text.DateFormat>()
    private val percentFormatter = NumberFormat.getPercentInstance()

    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    override val is24Hours: Boolean get() = android.text.format.DateFormat.is24HourFormat(context)

    override fun getBatteryState(): Flow<BatteryState> = flow {
        while (currentCoroutineContext().isActive) {
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

    @OptIn(ExperimentalTime::class)
    override fun formatTime(
        time: LocalDateTime,
        style: TimeFormat,
        locale: PlatformLocale
    ): String {
        if (style == TimeFormat.None) return ""
        val key = "$style/$locale"
        var formatter = dateFormatters[key]
        if (formatter == null) {
            val pattern = style.pattern ?: android.text.format.DateFormat.getBestDateTimePattern(
                locale,
                style.skeleton
            )
            formatter = SimpleDateFormat(pattern, locale)
            dateFormatters[key] = formatter
        }
        val instant = time.toInstant(TimeZone.currentSystemDefault())
        return formatter.format(instant.epochSeconds * DateUtils.SECOND_IN_MILLIS)
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