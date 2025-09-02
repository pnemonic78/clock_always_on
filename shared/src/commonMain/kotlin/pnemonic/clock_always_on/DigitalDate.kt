package pnemonic.clock_always_on

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.text.DateFormat

@Composable
fun DigitalDate(
    time: Long,
    modifier: Modifier = Modifier,
    style: Int = DateFormat.DEFAULT,
    textColor: Color = Color.Unspecified,
    onClick: IntCallback? = null
) {
    val locale = Locale.current.platformLocale
    val formatter = DateFormat.getDateInstance(style, locale)
    val text = formatter.format(time)

    Text(
        modifier = modifier.clickable {
            onClick?.invoke(style)
        },
        text = text,
        fontSize = 20.sp,
        color = textColor,
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ClockTheme {
        DigitalDate(System.currentTimeMillis())
    }
}
