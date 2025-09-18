package pnemonic.clock_always_on

import androidx.compose.ui.graphics.Color

data class ClockConfiguration(
    val timeStyle: Int = ClockStyle.DIGITAL_STACKED_THIN,
    val is24Hours: Boolean = false,
    val isSeconds: Boolean = false,
    val isDate: Boolean = true,
    val dateStyle: Int = DateFormat.DEFAULT,
    val isBattery: Boolean = true,
    val isBounce: Boolean = false,
    val backgroundColor: Color = Color.Black,
    val textColor: Color = Color.White
)
