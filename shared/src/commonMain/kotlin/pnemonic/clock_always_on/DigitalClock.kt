package pnemonic.clock_always_on

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat

private val formatters: MutableMap<String, SimpleDateFormat> = mutableMapOf()

@Composable
fun DigitalClockSimple(
    pattern: String = "HH:mm",
    textColor: Color = Color.Unspecified,
    isThin: Boolean = true
) {
    val locale = Locale.current.platformLocale
    val formatterKey = "$pattern/$locale"
    var formatter = formatters[formatterKey]
    if (formatter == null) {
        formatter = SimpleDateFormat(pattern, locale)
        formatters[formatterKey] = formatter
    }
    var time by remember { mutableLongStateOf(System.currentTimeMillis()) }
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

    LaunchedEffect(time) {
        delay(DateUtils.SECOND_IN_MILLIS)
        time = System.currentTimeMillis()
    }
}

@Composable
fun DigitalClockStacked(
    patternHours: String = "HH",
    patternMinutes: String = "mm",
    patternSeconds: String = "ss",
    textColor: Color = Color.Unspecified,
    isThin: Boolean = true
) {
    val locale = Locale.current.platformLocale
    var time by remember { mutableLongStateOf(System.currentTimeMillis()) }

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

    LaunchedEffect(time) {
        delay(DateUtils.SECOND_IN_MILLIS)
        time = System.currentTimeMillis()
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        DigitalClockSimple()
    }
}
