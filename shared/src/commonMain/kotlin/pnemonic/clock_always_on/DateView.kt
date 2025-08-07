package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.text.DateFormat

@Composable
fun DateView() {
    var time by remember { mutableLongStateOf(System.currentTimeMillis()) }

    DigitalDate(
        time = time,
        style = DateFormat.DEFAULT
    )

    LaunchedEffect(time) {
        delay(DateUtils.SECOND_IN_MILLIS)
        time = System.currentTimeMillis()
    }
}