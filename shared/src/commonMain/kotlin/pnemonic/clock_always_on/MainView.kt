package pnemonic.clock_always_on

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay

@Composable
fun MainView(modifier: Modifier = Modifier.fillMaxSize()) {
    val platform = rememberPlatform()
    val viewModel = viewModel { ClockViewModel(platform) }
    val configuration by viewModel.configuration.collectAsState()
    val batteryState by viewModel.batteryState.collectAsState()

    val bounce by viewModel.bounces.collectAsState()
    if ((deltaBouncePx == 0f) || (bounce.deltaX == 0f)) {
        with(LocalDensity.current) {
            deltaBouncePx = deltaBounce.toPx()
            viewModel.updateBounce(
                deltaX = deltaBouncePx,
                deltaY = deltaBouncePx
            )
        }
    }

    Column(
        modifier = modifier
            .background(color = configuration.backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
                .onSizeChanged { viewModel.updateBounce(screenSize = it) }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .onGloballyPositioned { coordinates ->
                        val offset = coordinates.positionInParent()
                        viewModel.updateBounce(box = Rect(offset, coordinates.size.toSize()))
                    }
                    .offset { bounce.offset },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ClockView(
                    platform = platform,
                    style = configuration.timeStyle,
                    is24Hours = configuration.is24Hours,
                    isSeconds = configuration.isSeconds,
                    textColor = configuration.textColor,
                    onClick = { viewModel.onClockClick(it) }
                )
                if (configuration.isDate) {
                    Spacer(modifier = Modifier.height(8.dp))
                    DateView(
                        style = configuration.dateStyle,
                        color = configuration.textColor,
                        onClick = { viewModel.onDateClick(it) }
                    )
                }
                if (configuration.isBattery) {
                    Spacer(modifier = Modifier.height(8.dp))
                    BatteryStatus(batteryState, color = configuration.textColor)
                }
            }
        }
        SettingsBar(
            configuration = configuration,
            listener = viewModel
        )
    }

    if (configuration.isBounce) {
        LaunchedEffect(bounce) {
            delay(DateUtils.SECOND_IN_MILLIS)
            viewModel.bounce()
        }
    } else {
        viewModel.resetBounce()
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ClockTheme {
        MainView()
    }
}
