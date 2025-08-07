package pnemonic.clock_always_on

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

private fun createDataStore(context: Context): DataStorePreferences = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)

@Composable
actual fun rememberDataStore(): DataStorePreferences {
    val context = LocalContext.current
    return remember { createDataStore(context) }
}