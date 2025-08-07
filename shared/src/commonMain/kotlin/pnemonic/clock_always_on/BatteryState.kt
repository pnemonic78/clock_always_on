package pnemonic.clock_always_on

data class BatteryState(
    val level: Int = -1,
    val charging: Boolean = false
)
