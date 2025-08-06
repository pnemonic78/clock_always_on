package pnemonic.clock_always_on

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

private const val SECOND_IN_MILLIS = 1000L
private const val MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60

@Composable
fun MainView() {
    var time by remember { mutableLongStateOf(System.currentTimeMillis()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ClockView(time = time)
    }

    LaunchedEffect(time) {
        delay(SECOND_IN_MILLIS)
        time = System.currentTimeMillis()
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        MainView()
    }
}
