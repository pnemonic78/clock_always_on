package pnemonic.clock_always_on

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import java.text.DateFormat

@Composable
fun DigitalClock(
    time: Long,
    isSeconds: Boolean = false,
    is24HourMode: Boolean = false,
    textColor: Color = Color.White
) {
//    val s24 = if (is24HourMode) {
//        getBestDateTimePattern("Hm")
//    }
//     else {
//        getBestDateTimePattern("hm")
//    }
    val formatter = if (isSeconds) {
        DateFormat.getTimeInstance(DateFormat.MEDIUM)
    }
    else {
        DateFormat.getTimeInstance(DateFormat.SHORT)
    }
    val text = formatter.format(time)

    Text(
        text = text,
        fontSize = 80.sp,
        color = textColor
    )
}

@Preview
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        DigitalClock(System.currentTimeMillis())
    }
}
