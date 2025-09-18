package pnemonic.clock_always_on

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDateTime

@Composable
fun DigitalClockSimple(
    time: LocalDateTime,
    style: TimeFormat = TimeFormat.HoursAndMinutes,
    textColor: Color = Color.Companion.Unspecified,
    isThin: Boolean = true
) {
    val locale = Locale.Companion.current.platformLocale
    val platform = LocalPlatform.current
    val text = platform.formatTime(time, style, locale)

    DigitalClockSimple(text, textColor, isThin)
}

@Composable
private fun DigitalClockSimple(
    text: String,
    textColor: Color = Color.Companion.Unspecified,
    isThin: Boolean = true
) {
    Text(
        text = text,
        fontSize = 70.sp,
        color = textColor,
        textAlign = TextAlign.Companion.Center,
        maxLines = 1,
        softWrap = false,
        fontWeight = if (isThin) FontWeight.Companion.Thin else FontWeight.Companion.Normal
    )
}