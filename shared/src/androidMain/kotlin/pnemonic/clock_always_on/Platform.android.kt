package pnemonic.clock_always_on

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow
import java.util.Locale

class AndroidPlatform(private val context: Context) : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    override val is24Hours: Boolean get() = DateFormat.is24HourFormat(context)

    override fun getBestDateTimePattern(locale: Locale, skeleton: String): String {
        return DateFormat.getBestDateTimePattern(locale, skeleton)
    }

    override fun getBatteryState(): Flow<BatteryState> {
        TODO("Not yet implemented")
    }
}

//actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun rememberPlatform(): Platform {
    val context = LocalContext.current
    return remember { AndroidPlatform(context) }
}