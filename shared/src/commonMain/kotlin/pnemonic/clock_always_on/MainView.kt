package pnemonic.clock_always_on

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

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
            //TODO show color picker
            //TODO scope.launch { ccd.setBackgroundColor(it) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = configuration.backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
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
                DateView()
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
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        MainView()
    }
}
