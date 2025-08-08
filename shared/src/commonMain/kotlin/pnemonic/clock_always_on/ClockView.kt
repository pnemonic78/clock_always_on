package pnemonic.clock_always_on

import androidx.compose.material3.MaterialTheme
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
    pattern: String = "HH:mm",
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    var time by remember { mutableLongStateOf(System.currentTimeMillis()) }

    if (isDigital) {
        DigitalClock(
            time = time,
            pattern = pattern,
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
