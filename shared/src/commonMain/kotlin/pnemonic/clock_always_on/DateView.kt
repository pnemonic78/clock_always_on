package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDate

@Composable
fun DateView(
    date: LocalDate,
    modifier: Modifier = Modifier,
    style: Int = DateFormat.DEFAULT,
    platform: Platform,
    color: Color = Color.Unspecified,
    onClick: IntCallback? = null
) {
    DigitalDate(
        modifier = modifier,
        date = date,
        platform = platform,
        style = style,
        textColor = color,
        onClick = onClick
    )
}
