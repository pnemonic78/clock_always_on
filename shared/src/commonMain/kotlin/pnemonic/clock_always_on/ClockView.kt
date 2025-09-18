package pnemonic.clock_always_on

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDateTime

val clockSizeMin = 150.dp
val clockSizeMax = 350.dp

@Composable
fun ClockView(
    time: LocalDateTime,
    modifier: Modifier = Modifier,
    style: Int = ClockStyle.DIGITAL_STACKED_THIN,
    is24Hours: Boolean = false,
    isSeconds: Boolean = false,
    textColor: Color = Color.Unspecified,
    onClick: IntCallback? = null,
    size: Dp = clockSizeMin
) {
    Box(
        modifier = modifier
            .clickable { onClick?.invoke(style) }) {
        when (style) {
            ClockStyle.DIGITAL_STACKED,
            ClockStyle.DIGITAL_STACKED_THIN -> {
                val styleHours = if (is24Hours) TimeFormat.Hours else TimeFormat.Hours12
                val styleMinutes = TimeFormat.Minutes
                val styleSeconds = if (isSeconds) TimeFormat.Seconds else TimeFormat.None

                DigitalClockStacked(
                    time = time,
                    textColor = textColor,
                    styleHours = styleHours,
                    styleMinutes = styleMinutes,
                    styleSeconds = styleSeconds,
                    isThin = (style == ClockStyle.DIGITAL_STACKED_THIN)
                )
            }

            ClockStyle.ANALOG_SIMPLE -> AnalogClockMaterial(
                modifier = Modifier.size(size),
                time = time,
                isSeconds = isSeconds,
                color = textColor
            )

            ClockStyle.ANALOG_TICKS -> AnalogClockTicks(
                modifier = Modifier.size(size),
                time = time,
                isSeconds = isSeconds,
                color = textColor
            )

            else -> {
                val styleTime = if (is24Hours) {
                    if (isSeconds) TimeFormat.HoursAndMinutesAndSeconds else TimeFormat.HoursAndMinutes
                } else {
                    if (isSeconds) TimeFormat.HoursAndMinutesAndSeconds12 else TimeFormat.HoursAndMinutes12
                }

                DigitalClockSimple(
                    time = time,
                    textColor = textColor,
                    style = styleTime,
                    isThin = (style == ClockStyle.DIGITAL_SIMPLE_THIN)
                )
            }
        }
    }
}
