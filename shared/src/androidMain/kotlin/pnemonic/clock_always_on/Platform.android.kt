package pnemonic.clock_always_on

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

private const val delayBattery = DateUtils.SECOND_IN_MILLIS * 5

class AndroidPlatform(private val context: Context) : Platform {

    private var dataStore: DataStorePreferences? = null

    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"

    override val is24Hours: Boolean get() = DateFormat.is24HourFormat(context)

    override fun getBestDateTimePattern(locale: Locale, skeleton: String): String {
        return DateFormat.getBestDateTimePattern(locale, skeleton)
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
}

@Composable
actual fun rememberPlatform(): Platform {
    val context = LocalContext.current
    return remember { AndroidPlatform(context) }
}