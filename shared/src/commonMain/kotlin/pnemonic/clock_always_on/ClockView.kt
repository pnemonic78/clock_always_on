package pnemonic.clock_always_on

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview

object ClockStyle {
    const val DIGITAL_STACKED_THIN = 0
    const val DIGITAL_STACKED = 1
    const val DIGITAL_SIMPLE_THIN = 2
    const val DIGITAL_SIMPLE = 3
    const val ANALOG = 4
}

@Composable
fun ClockView(
    platform: Platform,
    style: Int = ClockStyle.DIGITAL_STACKED_THIN,
    is24Hours: Boolean = false,
    isSeconds: Boolean = false,
    textColor: Color = Color.Unspecified,
    onClick: IntCallback? = null
) {
    val locale = Locale.current.platformLocale

    Box(modifier = Modifier.clickable {
        onClick?.invoke(style)
    }) {
        when (style) {
            ClockStyle.DIGITAL_STACKED,
            ClockStyle.DIGITAL_STACKED_THIN -> {
                val skeletonHours = if (is24Hours) "HH" else "hh"
                val patternHours = platform.getBestDateTimePattern(locale, skeletonHours)
                val patternMinutes = "mm"
                val patternSeconds = if (isSeconds) "ss" else ""

                DigitalClockStacked(
                    textColor = textColor,
                    patternHours = patternHours,
                    patternMinutes = patternMinutes,
                    patternSeconds = patternSeconds,
                    isThin = (style == ClockStyle.DIGITAL_STACKED_THIN)
                )
            }

            //ClockStyle.ANALOG -> {
            //TODO AnalogClock(isSeconds = isSeconds, color = textColor)
            //}

            else -> {
                val skeletonSimple = if (is24Hours) {
                    if (isSeconds) "Hms" else "Hm"
                } else {
                    if (isSeconds) "hms" else "hm"
                }
                val patternSimple = platform.getBestDateTimePattern(locale, skeletonSimple)

                DigitalClockSimple(
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
    MyApplicationTheme {
        val platform = rememberPlatform()
        ClockView(platform = platform)
    }
}
