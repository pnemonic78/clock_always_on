package pnemonic.clock_always_on

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class AndroidPlatform(private val context: Context) : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
    override val is24Hours: Boolean get() = DateFormat.is24HourFormat(context)
}

//actual fun getPlatform(): Platform = AndroidPlatform()

@Composable
actual fun rememberPlatform(): Platform {
    val context = LocalContext.current
    return remember { AndroidPlatform(context) }
}