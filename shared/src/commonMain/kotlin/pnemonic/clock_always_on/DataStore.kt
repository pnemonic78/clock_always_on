package pnemonic.clock_always_on

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

typealias DataStorePreferences = DataStore<Preferences>

/**
 *  Gets the singleton DataStore instance, creating it if necessary.
 */
fun createDataStore(producePath: () -> String): DataStorePreferences =
    PreferenceDataStoreFactory.createWithPath(
        produceFile = { producePath().toPath() }
    )

internal const val dataStoreFileName = "clock.preferences_pb"
