package pnemonic.clock_always_on

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.text.DateFormat

private val formatter = DateFormat.getTimeInstance(DateFormat.SHORT)

@Composable
fun ClockView(time: Long = System.currentTimeMillis()) {
    val text = formatter.format(time)
    Text(
        text = text,
        fontSize = 80.sp
    )
}

@Preview
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        ClockView()
    }
}
