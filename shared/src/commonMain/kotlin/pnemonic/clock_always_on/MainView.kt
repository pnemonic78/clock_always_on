package pnemonic.clock_always_on

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

private val spacerSize = 8.dp

@Composable
fun MainView(modifier: Modifier = Modifier.fillMaxSize()) {
    val platform = rememberPlatform()
    val viewModel = viewModel { ClockViewModel(platform) }
    val configuration by viewModel.configuration.collectAsState()
    val batteryState by viewModel.batteryState.collectAsState()

    val density = LocalDensity.current
    var screenSize by remember { mutableStateOf(IntSize.Zero) }
    var dateHeight by remember { mutableIntStateOf(0) }
    var batteryHeight by remember { mutableIntStateOf(0) }
    var clockSize by remember { mutableStateOf(null as Dp?) }

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

    val settingsVisible by viewModel.settingsVisible.collectAsState()

    Box(
        modifier = modifier
            .background(color = configuration.backgroundColor)
            .onSizeChanged { size ->
                viewModel.setScreenSize(screenSize = size)
                if (size != screenSize) {
                    screenSize = size
                    clockSize = measureClockSize(screenSize, dateHeight, batteryHeight, density)
                }
            }
            .clickable { viewModel.onMainViewClick() }
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
                size = clockSize,
                onClick = { viewModel.onClockClick(it) }
            )
            if (configuration.isDate) {
                Spacer(modifier = Modifier.height(spacerSize))
                DateView(
                    modifier = Modifier.onSizeChanged { size ->
                        if (size.height != dateHeight) {
                            dateHeight = size.height
                            clockSize =
                                measureClockSize(screenSize, dateHeight, batteryHeight, density)
                        }
                    },
                    style = configuration.dateStyle,
                    color = configuration.textColor,
                    onClick = { viewModel.onDateClick(it) }
                )
            } else {
                dateHeight = 0
            }
            if (configuration.isBattery) {
                Spacer(modifier = Modifier.height(spacerSize))
                BatteryStatus(
                    modifier = Modifier.onSizeChanged { size ->
                        if (size.height != batteryHeight) {
                            batteryHeight = size.height
                            clockSize =
                                measureClockSize(screenSize, dateHeight, batteryHeight, density)
                        }
                    },
                    state = batteryState,
                    color = configuration.textColor
                )
            } else {
                batteryHeight = 0
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            visible = settingsVisible,
            enter = fadeIn(),
            exit = fadeOut(
                animationSpec = tween(durationMillis = ClockViewModel.SETTINGS_FADE)
            ),
            label = "settings"
        ) {
            SettingsBar(
                modifier = Modifier
                    .clickable { viewModel.onSettingsBarClick() },
                configuration = configuration,
                listener = viewModel
            )
        }
    }
}

private fun measureClockSize(
    screenSize: IntSize,
    dateHeight: Int,
    batteryHeight: Int,
    density: Density
): Dp {
    val screenHeight = min(screenSize.height, screenSize.width)
    if (screenHeight == 0) return clockSizeMin

    val spacerSizePx = with(density) { spacerSize.toPx().roundToInt() }
    val clockSizeMinPx = with(density) { clockSizeMin.toPx().roundToInt() }
    val clockSizeMaxPx = with(density) { clockSizeMax.toPx().roundToInt() }

    var clockHeight = screenHeight - spacerSizePx
    if (dateHeight > 0) {
        clockHeight -= spacerSizePx + dateHeight
    }
    if (batteryHeight > 0) {
        clockHeight -= spacerSizePx + batteryHeight
    }
    val clockSize = min(max(clockSizeMinPx, clockHeight), clockSizeMaxPx)
    return with(density) { clockSize.toDp() }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ClockTheme {
        MainView()
    }
}
