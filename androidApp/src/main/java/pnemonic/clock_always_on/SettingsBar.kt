package pnemonic.clock_always_on

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private val buttonSize = 40.dp
private val buttonSpace = 8.dp

interface SettingsBarListener {
    val on24HourClick: BooleanCallback
    val onSecondsClick: BooleanCallback
    val onDateClick: BooleanCallback
    val onBatteryClick: BooleanCallback
    val onBounceClick: BooleanCallback
    val onTextColorClick: ColorCallback
    val onBackgroundClick: ColorCallback
}

@Composable
fun SettingsBar(
    modifier: Modifier = Modifier,
    configuration: ClockConfiguration,
    listener: SettingsBarListener
) {
    Row(modifier = modifier.wrapContentSize()) {
        IconButton24(configuration.is24Hours, onClick = listener.on24HourClick)
        Spacer(modifier = Modifier.width(buttonSpace))
        IconButtonSeconds(configuration.isSeconds, onClick = listener.onSecondsClick)
        Spacer(modifier = Modifier.width(buttonSpace))
        IconButtonDate(configuration.isDate, onClick = listener.onDateClick)
        Spacer(modifier = Modifier.width(buttonSpace))
        IconButtonBattery(configuration.isBattery, onClick = listener.onBatteryClick)
        Spacer(modifier = Modifier.width(buttonSpace))
        IconButtonBounce(configuration.isBounce, onClick = listener.onBounceClick)
        Spacer(modifier = Modifier.width(buttonSpace))
        IconButtonTextColor(configuration.textColor, onClick = listener.onTextColorClick)
        Spacer(modifier = Modifier.width(buttonSpace))
        IconButtonBackgroundColor(
            configuration.backgroundColor,
            onClick = listener.onBackgroundClick
        )
    }
}

@Composable
private fun IconButton24(selected: Boolean, onClick: BooleanCallback) {
    val textSize = with(LocalDensity.current) {
        24.dp.toSp()
    }
    IconButton(
        onClick = { onClick.invoke(!selected) }
    ) {
        Text(
            text = if (selected) "24" else "12",
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun IconButtonSeconds(selected: Boolean, onClick: BooleanCallback) {
    IconButton(
        onClick = { onClick.invoke(!selected) }
    ) {
        Icon(
            modifier = Modifier.size(buttonSize),
            painter = if (selected)
                painterResource(id = R.drawable.ic_timer)
            else
                painterResource(id = R.drawable.ic_timer_off),
            contentDescription = "seconds"
        )
    }
}

@Composable
private fun IconButtonBounce(selected: Boolean, onClick: BooleanCallback) {
    IconButton(
        onClick = { onClick.invoke(!selected) },
        enabled = false
    ) {
        Icon(
            modifier = Modifier.size(buttonSize),
            painter = if (selected)
                painterResource(id = R.drawable.ic_animation_on)
            else
                painterResource(id = R.drawable.ic_animation_off),
            contentDescription = "bounce"
        )
    }
}

@Composable
private fun IconButtonDate(selected: Boolean, onClick: BooleanCallback) {
    IconButton(
        onClick = { onClick.invoke(!selected) }
    ) {
        Icon(
            modifier = Modifier.size(buttonSize),
            painter = if (selected)
                painterResource(id = R.drawable.ic_date_on)
            else
                painterResource(id = R.drawable.ic_date_off),
            contentDescription = "date"
        )
    }
}

@Composable
private fun IconButtonBattery(selected: Boolean, onClick: BooleanCallback) {
    IconButton(
        onClick = { onClick.invoke(!selected) }
    ) {
        Icon(
            modifier = Modifier.size(buttonSize),
            painter = if (selected)
                painterResource(id = R.drawable.ic_battery_on)
            else
                painterResource(id = R.drawable.ic_battery_off),
            contentDescription = "battery"
        )
    }
}

@Composable
private fun IconButtonTextColor(color: Color, onClick: ColorCallback) {
    IconButton(
        onClick = { onClick.invoke(color) },
        enabled = false
    ) {
        Icon(
            modifier = Modifier.size(buttonSize),
            painter = painterResource(id = R.drawable.ic_color_text),
            contentDescription = "text color"
        )
    }
}

@Composable
private fun IconButtonBackgroundColor(color: Color, onClick: ColorCallback) {
    IconButton(
        onClick = { onClick.invoke(color) },
        enabled = false
    ) {
        Icon(
            modifier = Modifier.size(buttonSize),
            painter = painterResource(id = R.drawable.ic_color_fill),
            contentDescription = "background color"
        )
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    val configuration = ClockConfiguration()
    val listener = object : SettingsBarListener {
        override val on24HourClick: BooleanCallback = {}
        override val onSecondsClick: BooleanCallback = {}
        override val onDateClick: BooleanCallback = {}
        override val onBatteryClick: BooleanCallback = {}
        override val onBounceClick: BooleanCallback = {}
        override val onTextColorClick: ColorCallback = {}
        override val onBackgroundClick: ColorCallback = {}
    }
    MyApplicationTheme {
        SettingsBar(configuration = configuration, listener = listener)
    }
}
