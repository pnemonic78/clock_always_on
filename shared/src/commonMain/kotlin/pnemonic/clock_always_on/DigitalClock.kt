package pnemonic.clock_always_on

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar

private val formatters: MutableMap<String, SimpleDateFormat> = mutableMapOf()

@Composable
fun DigitalClockSimple(
    calendar: Calendar,
    pattern: String = "HH:mm",
    textColor: Color = Color.Unspecified,
    isThin: Boolean = true
) {
    val time = calendar.time
    val locale = Locale.current.platformLocale
    val formatterKey = "$pattern/$locale"
    var formatter = formatters[formatterKey]
    if (formatter == null) {
        formatter = SimpleDateFormat(pattern, locale)
        formatters[formatterKey] = formatter
    }
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
    calendar: Calendar,
    patternHours: String = "HH",
    patternMinutes: String = "mm",
    patternSeconds: String = "ss",
    textColor: Color = Color.Unspecified,
    isThin: Boolean = true
) {
    val time = calendar.time
    val locale = Locale.current.platformLocale

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val formatterHoursKey = "$patternHours/$locale"
        var formatterHours = formatters[formatterHoursKey]
        if (formatterHours == null) {
            formatterHours = SimpleDateFormat(patternHours, locale)
            formatters[formatterHoursKey] = formatterHours
        }
        val textHours = formatterHours.format(time)
        Text(
            text = textHours,
            fontSize = 80.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = if (isThin) FontWeight.Thin else FontWeight.Normal
        )

        val formatterMinutesKey = "$patternMinutes/$locale"
        var formatterMinutes = formatters[formatterMinutesKey]
        if (formatterMinutes == null) {
            formatterMinutes = SimpleDateFormat(patternMinutes, locale)
            formatters[formatterMinutesKey] = formatterMinutes
        }
        val textMinutes = formatterMinutes.format(time)
        Text(
            text = textMinutes,
            fontSize = 80.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            fontWeight = if (isThin) FontWeight.Thin else FontWeight.Normal
        )

        if (!patternSeconds.isEmpty()) {
            val formatterSecondsKey = "$patternSeconds/$locale"
            var formatterSeconds = formatters[formatterSecondsKey]
            if (formatterSeconds == null) {
                formatterSeconds = SimpleDateFormat(patternSeconds, locale)
                formatters[formatterSecondsKey] = formatterSeconds
            }
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

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ClockTheme {
        DigitalClockSimple(calendar = Calendar.getInstance())
    }
}
