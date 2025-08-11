package pnemonic.clock_always_on

import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color
import java.text.DateFormat

data class ClockConfiguration(
    val timeStyle: Int = ClockStyle.DIGITAL_STACKED_THIN,
    val is24Hours: Boolean = false,
    val isSeconds: Boolean = false,
    val isDate: Boolean = true,
    @IntRange(0, 3)
    val dateStyle: Int = DateFormat.DEFAULT,
    val isBattery: Boolean = true,
    val isBounce: Boolean = false,
    val backgroundColor: Color = Color.Black,
    val textColor: Color = Color.White
)
