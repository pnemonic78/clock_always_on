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
fun DigitalClockSimple(
    time: LocalDateTime,
    pattern: String = "HH:mm",
    textColor: Color = Color.Unspecified,
    isThin: Boolean = true
) {
    val locale = Locale.current.platformLocale
    val formatter = DateFormat.getInstance(pattern, locale)
    val text = formatter.format(time)

    Text(
        text = text,
        fontSize = 70.sp,
        color = textColor,
        textAlign = TextAlign.Center,
        maxLines = 1,
        softWrap = false,
        fontWeight = if (isThin) FontWeight.Thin else FontWeight.Normal
    )
}

@Composable
fun DigitalClockStacked(
    time: LocalDateTime,
    patternHours: String = "HH",
    patternMinutes: String = "mm",
    patternSeconds: String = "ss",
    textColor: Color = Color.Unspecified,
    isThin: Boolean = true
) {
    val locale = Locale.current.platformLocale
    val formatterHours = DateFormat.getInstance(patternHours, locale)
    val textHours = formatterHours.format(time)
    val formatterMinutes = DateFormat.getInstance(patternMinutes, locale)
    val textMinutes = formatterMinutes.format(time)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = textHours,
            fontSize = 80.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = if (isThin) FontWeight.Thin else FontWeight.Normal
        )
        Text(
            text = textMinutes,
            fontSize = 80.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = if (isThin) FontWeight.Thin else FontWeight.Normal
        )

        if (!patternSeconds.isEmpty()) {
            val formatterSeconds = DateFormat.getInstance(patternSeconds, locale)
            val textSeconds = formatterSeconds.format(time)
            Text(
                text = textSeconds,
                fontSize = 80.sp,
                color = textColor,
                textAlign = TextAlign.Center,
                fontWeight = if (isThin) FontWeight.Thin else FontWeight.Normal
            )
        }
    }
}
