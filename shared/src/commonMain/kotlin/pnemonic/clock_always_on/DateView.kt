package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import java.text.DateFormat
import java.util.Calendar

@Composable
fun DateView(
    date: Long,
    modifier: Modifier = Modifier,
    style: Int = DateFormat.DEFAULT,
    color: Color = Color.Unspecified,
    onClick: IntCallback? = null
) {
    val locale = Locale.current.platformLocale
    val calendar = remember { Calendar.getInstance(locale) }
    calendar.timeInMillis = date

    DigitalDate(
        modifier = modifier,
        calendar = calendar,
        style = style,
        textColor = color,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ClockTheme {
        DateView(date = System.currentTimeMillis())
    }
}
