package pnemonic.clock_always_on

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import java.util.Calendar

object ClockStyle {
    const val DIGITAL_STACKED_THIN = 0
    const val DIGITAL_STACKED = 1
    const val DIGITAL_SIMPLE_THIN = 2
    const val DIGITAL_SIMPLE = 3
    const val ANALOG_SIMPLE = 4
    const val ANALOG_TICKS = 5
}

private val clockSize = 300.dp

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
    var time by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val calendar = remember { Calendar.getInstance(locale) }
    calendar.timeInMillis = time

    Box(modifier = Modifier.clickable {
        onClick?.invoke(style)
    }) {
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
                modifier = Modifier.size(clockSize),
                calendar = calendar,
                isSeconds = isSeconds,
                color = textColor
            )

            ClockStyle.ANALOG_TICKS -> AnalogClockTicks(
                modifier = Modifier.size(clockSize),
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

    LaunchedEffect(time) {
        delay(DateUtils.SECOND_IN_MILLIS)
        time = System.currentTimeMillis()
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ClockTheme {
        val platform = rememberPlatform()
        ClockView(platform = platform)
    }
}
