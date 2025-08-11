package pnemonic.clock_always_on

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import java.text.DateFormat

@Composable
fun DateView(
    style: Int = DateFormat.DEFAULT,
    onClick: IntCallback? = null
) {
    var time by remember { mutableLongStateOf(System.currentTimeMillis()) }

    DigitalDate(
        time = time,
        style = style,
        onClick = onClick
    )

    LaunchedEffect(time) {
        delay(DateUtils.SECOND_IN_MILLIS)
        time = System.currentTimeMillis()
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        DateView()
    }
}
