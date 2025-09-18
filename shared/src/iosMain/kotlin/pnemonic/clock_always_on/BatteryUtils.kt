package pnemonic.clock_always_on

import platform.UIKit.UIDeviceBatteryState
import kotlin.math.roundToInt

/**
 * Battery utilities.
 */
object BatteryUtils {
    fun getState(level: Float, batteryState: UIDeviceBatteryState): BatteryState {
        return BatteryState(
            level = (level * 100).roundToInt(),
            charging = (batteryState == UIDeviceBatteryState.UIDeviceBatteryStateCharging)
        )
    }
}