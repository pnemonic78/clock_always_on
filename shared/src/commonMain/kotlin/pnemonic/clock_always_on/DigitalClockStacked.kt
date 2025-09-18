package pnemonic.clock_always_on

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDateTime

@Composable
fun DigitalClockStacked(
    time: LocalDateTime,
    styleHours: TimeFormat = TimeFormat.Hours,
    styleMinutes: TimeFormat = TimeFormat.Minutes,
    styleSeconds: TimeFormat = TimeFormat.None,
    textColor: Color = Color.Companion.Unspecified,
    isThin: Boolean = true
) {
    val locale = Locale.Companion.current.platformLocale
    val platform = LocalPlatform.current
    val textHours = platform.formatTime(time, styleHours, locale)
    val textMinutes = platform.formatTime(time, styleMinutes, locale)
    val textSeconds = platform.formatTime(time, styleSeconds, locale)

    Column(horizontalAlignment = Alignment.Companion.CenterHorizontally) {
        Text(
            text = textHours,
            fontSize = 80.sp,
            color = textColor,
            textAlign = TextAlign.Companion.Center,
            fontWeight = if (isThin) FontWeight.Companion.Thin else FontWeight.Companion.Normal
        )
        Text(
            text = textMinutes,
            fontSize = 80.sp,
            color = textColor,
            textAlign = TextAlign.Companion.Center,
            fontWeight = if (isThin) FontWeight.Companion.Thin else FontWeight.Companion.Normal
        )

        if (!textSeconds.isEmpty()) {
            Text(
                text = textSeconds,
                fontSize = 80.sp,
                color = textColor,
                textAlign = TextAlign.Companion.Center,
                fontWeight = if (isThin) FontWeight.Companion.Thin else FontWeight.Companion.Normal
            )
        }
    }
}