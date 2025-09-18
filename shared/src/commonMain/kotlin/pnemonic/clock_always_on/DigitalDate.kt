package pnemonic.clock_always_on

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import kotlinx.datetime.LocalDate

@Composable
fun DigitalDate(
    date: LocalDate,
    modifier: Modifier = Modifier,
    style: Int = DateFormat.DEFAULT,
    textColor: Color = Color.Unspecified,
    onClick: IntCallback? = null
) {
    val locale = Locale.current.platformLocale
    val platform = LocalPlatform.current
    val formatted = platform.formatDate(date, style, locale)

    Text(
        modifier = modifier.clickable { onClick?.invoke(style) },
        text = formatted,
        fontSize = 20.sp,
        color = textColor,
        textAlign = TextAlign.Center
    )
}
