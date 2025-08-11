package pnemonic.clock_always_on

import android.content.Context

internal fun createDataStore(context: Context): DataStorePreferences = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)
