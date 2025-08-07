package pnemonic.clock_always_on

import androidx.compose.ui.graphics.Color

data class ClockConfiguration(
    val isDigital: Boolean = true,
    val is24Hours: Boolean = false,
    val isSeconds: Boolean = false,
    val isDate: Boolean = true,
    val isBattery: Boolean = true,
    val isBounce: Boolean = false,
    val backgroundColor: Color = Color.Black,
    val textColor: Color = Color.White
)
