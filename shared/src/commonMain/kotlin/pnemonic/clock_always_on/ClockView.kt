package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

@Composable
fun ClockView(
    isDigital: Boolean = true,
    isSeconds: Boolean = false,
    is24Hours: Boolean = false,
    textColor: Color = Color.White
) {
    var time by remember { mutableLongStateOf(System.currentTimeMillis()) }

    if (isDigital) {
        DigitalClock(
            time = time,
            isSeconds = isSeconds,
            is24HourMode = is24Hours,
            textColor = textColor
        )
    } else {
        //TODO AnalogClock(time = time, isSeconds = isSeconds, color = textColor)
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
        ClockView()
    }
}
