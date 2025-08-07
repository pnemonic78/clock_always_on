package pnemonic.clock_always_on

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

@Composable
fun MainView() {
    val scope = rememberCoroutineScope()
    val ds = rememberDataStore()
    val cd = rememberClockConfigurationData(ds)
    val configuration by cd.configuration().collectAsState(ClockConfiguration())

    val settingsListener = object : SettingsBarListener {
        override val on24HourClick: BooleanCallback = {
            scope.launch { cd.set24Hours(it) }
        }
        override val onSecondsClick: BooleanCallback = {
            scope.launch { cd.setSeconds(it) }
        }
        override val onDateClick: BooleanCallback = {
            scope.launch { cd.setDate(it) }
        }
        override val onBatteryClick: BooleanCallback = {
            scope.launch { cd.setBattery(it) }
        }
        override val onBounceClick: BooleanCallback = {
            scope.launch { cd.setBounce(it) }
        }
        override val onTextColorClick: ColorCallback = {
            //TODO show color picker
            //TODO scope.launch { cd.setTextColor(it) }
        }
        override val onBackgroundClick: ColorCallback = {
            //TODO show color picker
            //TODO scope.launch { cd.setBackgroundColor(it) }
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
                isSeconds = configuration.isSeconds,
                is24Hours = configuration.is24Hours,
                textColor = configuration.textColor
            )
            if (configuration.isDate) {
                // TODO DateView()
            }
            if (configuration.isBattery) {
                // TODO BatteryStatus()
            }
        }
        SettingsBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            configuration = configuration,
            listener = settingsListener
        )
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        MainView()
    }
}
