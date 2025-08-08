package pnemonic.clock_always_on

import androidx.annotation.IntRange

data class BatteryState(
    @IntRange(from = -1, to = 100)
    val level: Int = -1,
    val charging: Boolean = false
)
