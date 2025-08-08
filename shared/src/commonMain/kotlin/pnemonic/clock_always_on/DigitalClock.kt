package pnemonic.clock_always_on

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat

private val formatters: MutableMap<String, SimpleDateFormat> = mutableMapOf()

@Composable
fun DigitalClock(
    time: Long,
    pattern: String = "HH:mm",
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val locale = Locale.current.platformLocale
    val formattersKey = "$pattern/$locale"
    var formatter = formatters[formattersKey]
    if (formatter == null) {
        formatter = SimpleDateFormat(pattern, locale)
        formatters[formattersKey] = formatter
    }
    val text = formatter.format(time)

    Text(
        text = text,
        fontSize = 80.sp,
        color = textColor
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        DigitalClock(System.currentTimeMillis())
    }
}
