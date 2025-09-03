package pnemonic.clock_always_on

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.Calendar

object ClockStyle {
    const val DIGITAL_STACKED_THIN = 0
    const val DIGITAL_STACKED = 1
    const val DIGITAL_SIMPLE_THIN = 2
    const val DIGITAL_SIMPLE = 3
    const val ANALOG_SIMPLE = 4
    const val ANALOG_TICKS = 5
}

val clockSizeMin = 150.dp
val clockSizeMax = 350.dp

@Composable
fun ClockView(
    time: Long,
    modifier: Modifier = Modifier,
    platform: Platform,
    style: Int = ClockStyle.DIGITAL_STACKED_THIN,
    is24Hours: Boolean = false,
    isSeconds: Boolean = false,
    textColor: Color = Color.Unspecified,
    onClick: IntCallback? = null,
    size: Dp = clockSizeMin
) {
    val locale = Locale.current.platformLocale
    val calendar = remember { Calendar.getInstance(locale) }
    calendar.timeInMillis = time

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
                    calendar = calendar,
                    textColor = textColor,
                    patternHours = patternHours,
                    patternMinutes = patternMinutes,
                    patternSeconds = patternSeconds,
                    isThin = (style == ClockStyle.DIGITAL_STACKED_THIN)
                )
            }

            ClockStyle.ANALOG_SIMPLE -> AnalogClockMaterial(
                modifier = Modifier.size(size),
                calendar = calendar,
                isSeconds = isSeconds,
                color = textColor
            )

            ClockStyle.ANALOG_TICKS -> AnalogClockTicks(
                modifier = Modifier.size(size),
                calendar = calendar,
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
                    calendar = calendar,
                    textColor = textColor,
                    pattern = patternSimple,
                    isThin = (style == ClockStyle.DIGITAL_SIMPLE_THIN)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ClockTheme {
        val time = System.currentTimeMillis()
        val platform = rememberPlatform()
        ClockView(time = time, platform = platform)
    }
}
