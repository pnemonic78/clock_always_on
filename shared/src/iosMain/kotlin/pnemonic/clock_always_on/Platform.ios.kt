package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterFullStyle
import platform.Foundation.NSDateFormatterLongStyle
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSDateFormatterShortStyle
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterPercentStyle
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.UIKit.UIDevice
import kotlin.time.ExperimentalTime

private const val delayBattery = DateUtils.SECOND_IN_MILLIS * 30

class IOSPlatform : Platform {
    private val device = UIDevice.currentDevice
    private val dateFormatters = mutableMapOf<String, NSDateFormatter>()
    private val percentFormatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterPercentStyle
    }

    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override val is24Hours: Boolean get() = true//TODO("Not yet implemented")

    override fun getBatteryState(): Flow<BatteryState> = flow {
        device.batteryMonitoringEnabled = true
        if (device.batteryMonitoringEnabled) {
            while (currentCoroutineContext().isActive) {
                val state = BatteryUtils.getState(device.batteryLevel, device.batteryState)
                emit(state)
            }
            delay(delayBattery)
        } else {
            val state = BatteryUtils.getState(device.batteryLevel, device.batteryState)
            emit(state)
        }
    }

    override fun getDataStorePreferences(): DataStorePreferences {
        var ds = dataStore
        if (ds == null) {
            ds = createDataStore()
            dataStore = ds
        }
        return ds
    }

    override fun formatDate(
        date: LocalDate,
        style: Int,
        locale: PlatformLocale
    ): String {
        val key = "$style/$locale"
        var formatter = dateFormatters[key]
        if (formatter == null) {
            formatter = NSDateFormatter.new()!!
            when (style) {
                DateFormat.FULL -> formatter.setDateStyle(NSDateFormatterFullStyle)
                DateFormat.LONG -> formatter.setDateStyle(NSDateFormatterLongStyle)
                DateFormat.MEDIUM -> formatter.setDateStyle(NSDateFormatterMediumStyle)
                DateFormat.SHORT -> formatter.setDateStyle(NSDateFormatterShortStyle)
            }
            dateFormatters[key] = formatter
        }

        val secs = date.toEpochDays() * 86_400.0
        val nsdate = NSDate.dateWithTimeIntervalSince1970(secs)
        return formatter.stringFromDate(nsdate)
    }

    override fun formatPercent(value: Double): String {
        return percentFormatter.stringFromNumber(NSNumber(value)) ?: ""
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
            val pattern = style.pattern ?: NSDateFormatter.dateFormatFromTemplate(
                style.skeleton,
                0u,
                locale.toNSLocale()
            )!!
            formatter = NSDateFormatter.new()!!
            formatter.dateFormat = pattern
            dateFormatters[key] = formatter
        }

        val instant = time.toInstant(TimeZone.currentSystemDefault())
        val secs = instant.epochSeconds.toDouble()
        val nsdate = NSDate.dateWithTimeIntervalSince1970(secs)
        return formatter.stringFromDate(nsdate)
    }

    companion object {
        private var dataStore: DataStorePreferences? = null
    }
}

@Composable
actual fun rememberPlatform(): Platform {
    return remember { IOSPlatform() }
}