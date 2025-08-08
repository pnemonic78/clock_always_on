package pnemonic.clock_always_on

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.BatteryManager.BATTERY_STATUS_CHARGING
import android.os.BatteryManager.BATTERY_STATUS_UNKNOWN

/**
 * Battery utilities.
 */
object BatteryUtils {

    private var _batteryFilter: IntentFilter? = null

    val batteryFilter: IntentFilter
        get() {
            var filter = _batteryFilter
            if (filter == null) {
                filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
                _batteryFilter = filter
            }
            return filter
        }

    fun getIntent(context: Context): Intent? {
        return context.registerReceiver(null, batteryFilter)
    }

    fun getState(context: Context): BatteryState? {
        val intent = getIntent(context)
        val extras = intent?.extras ?: return null
        val level = extras.getInt(BatteryManager.EXTRA_LEVEL, -1)
        var scale = extras.getInt(BatteryManager.EXTRA_SCALE, 100)
        val status = extras.getInt(BatteryManager.EXTRA_STATUS, BATTERY_STATUS_UNKNOWN)
        if (scale == 0) scale = 100
        return BatteryState(
            level = (level * 100) / scale,
            charging = (status == BATTERY_STATUS_CHARGING)
        )
    }
}