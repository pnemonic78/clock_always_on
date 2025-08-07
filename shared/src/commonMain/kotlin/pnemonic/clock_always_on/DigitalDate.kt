package pnemonic.clock_always_on

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.text.DateFormat

@Composable
fun DigitalDate(
    time: Long,
    style: Int = DateFormat.DEFAULT,
    textColor: Color = Color.White
) {
    val formatter = DateFormat.getDateInstance(style)
    val text = formatter.format(time)

    Text(
        text = text,
        fontSize = 20.sp,
        color = textColor
    )
}

@Preview
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        DigitalDate(System.currentTimeMillis())
    }
}
