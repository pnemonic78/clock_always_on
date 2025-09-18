package pnemonic.clock_always_on

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
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
    val locale = Locale.current.platformLocale
    val platform = LocalPlatform.current

    Box(
        modifier = modifier
            .clickable { onClick?.invoke(style) }) {
        when (style) {
            ClockStyle.DIGITAL_STACKED,
            ClockStyle.DIGITAL_STACKED_THIN -> {
                val patternHours = if (is24Hours) "HH" else "hh"
                val patternMinutes = "mm"
                val patternSeconds = if (isSeconds) "ss" else ""

                DigitalClockStacked(
                    time = time,
                    textColor = textColor,
                    patternHours = patternHours,
                    patternMinutes = patternMinutes,
                    patternSeconds = patternSeconds,
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
                val skeletonSimple = if (is24Hours) {
                    if (isSeconds) "Hms" else "Hm"
                } else {
                    if (isSeconds) "hms" else "hm"
                }
                val patternSimple = platform.getBestDateTimePattern(locale, skeletonSimple)

                DigitalClockSimple(
                    time = time,
                    textColor = textColor,
                    pattern = patternSimple,
                    isThin = (style == ClockStyle.DIGITAL_SIMPLE_THIN)
                )
            }
        }
    }
}
