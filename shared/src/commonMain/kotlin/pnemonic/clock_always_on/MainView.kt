package pnemonic.clock_always_on

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import java.text.DateFormat

private val deltaBounce = 5.dp
private var deltaBouncePx = 5f

@Composable
fun MainView() {
    val locale = Locale.current.platformLocale
    val scope = rememberCoroutineScope()
    val platform = rememberPlatform()
    val dataStore = rememberDataStore()
    val ccd = rememberClockConfigurationData(dataStore)
    val configuration by ccd.configuration().collectAsState(
        ClockConfiguration(is24Hours = platform.is24Hours)
    )
    val skeleton = if (configuration.is24Hours) {
        if (configuration.isSeconds) "Hms" else "Hm"
    } else {
        if (configuration.isSeconds) "hms" else "hm"
    }
    val timePattern = platform.getBestDateTimePattern(locale, skeleton)
    val batteryStateFlow = if (configuration.isBattery) {
        platform.getBatteryState()
    } else {
        emptyFlow<BatteryState>()
    }
    val batteryState = batteryStateFlow.collectAsState(BatteryState())

    with(LocalDensity.current) {
        deltaBouncePx = deltaBounce.toPx()
    }
    var bounceTick by remember { mutableLongStateOf(0) }
    var screenSize by remember { mutableStateOf(IntSize.Zero) }
    var bounceOffset by remember { mutableStateOf(IntOffset.Zero) }
    var bounceBox by remember { mutableStateOf(Rect.Zero) }
    var bounceDeltaX by remember { mutableFloatStateOf(deltaBouncePx) }
    var bounceDeltaY by remember { mutableFloatStateOf(deltaBouncePx) }

    val settingsListener = object : SettingsBarListener {
        override val on24HourClick: BooleanCallback = {
            scope.launch { ccd.set24Hours(it) }
        }
        override val onSecondsClick: BooleanCallback = {
            scope.launch { ccd.setSeconds(it) }
        }
        override val onDateClick: BooleanCallback = {
            scope.launch { ccd.setDate(it) }
        }
        override val onBatteryClick: BooleanCallback = {
            scope.launch { ccd.setBattery(it) }
        }
        override val onBounceClick: BooleanCallback = {
            scope.launch { ccd.setBounce(it) }
        }
        override val onTextColorClick: ColorCallback = {
            //TODO show color picker
            //TODO scope.launch { ccd.setTextColor(it) }
        }
        override val onBackgroundClick: ColorCallback = {
            scope.launch { ccd.setBackgroundColor(it) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = configuration.backgroundColor)
            .onSizeChanged { screenSize = it }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .onGloballyPositioned { coordinates ->
                    val offset = coordinates.positionInParent()
                    bounceBox = Rect(offset, coordinates.size.toSize())
                }
                .offset { bounceOffset },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ClockView(
                isDigital = configuration.isDigital,
                pattern = timePattern,
                textColor = configuration.textColor
            )
            if (configuration.isDate) {
                Spacer(modifier = Modifier.height(8.dp))
                DateView(
                    style = configuration.dateStyle,
                    onClick = { style ->
                        var styleNext = style + 1
                        if (styleNext > DateFormat.SHORT) {
                            styleNext = DateFormat.FULL
                        }
                        scope.launch { ccd.setDateStyle(styleNext) }
                    }
                )
            }
            if (configuration.isBattery) {
                Spacer(modifier = Modifier.height(8.dp))
                BatteryStatus(batteryState.value)
            }
        }
        SettingsBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            configuration = configuration,
            listener = settingsListener
        )
    }

    if (configuration.isBounce) {
        LaunchedEffect(bounceTick) {
            delay(DateUtils.SECOND_IN_MILLIS)
            bounceTick++

            var tx = bounceOffset.x + bounceDeltaX
            var ty = bounceOffset.y + bounceDeltaY
            bounceOffset = IntOffset(tx.toInt(), ty.toInt())

            val b = bounceBox.translate(tx, ty)
            if (bounceDeltaX > 0) {
                if (b.right >= screenSize.width) {
                    bounceDeltaX = -deltaBouncePx
                }
            } else {
                if (b.left <= 0) {
                    bounceDeltaX = deltaBouncePx
                }
            }
            if (bounceDeltaY > 0) {
                if (b.bottom >= screenSize.height) {
                    bounceDeltaY = -deltaBouncePx
                }
            } else {
                if (b.top <= 0) {
                    bounceDeltaY = deltaBouncePx
                }
            }
        }
    } else {
        bounceTick = 0
        bounceOffset = IntOffset.Zero
        bounceDeltaX = deltaBouncePx
        bounceDeltaY = deltaBouncePx
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        MainView()
    }
}
