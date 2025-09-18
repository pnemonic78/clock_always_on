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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import clock_always_on.shared.generated.resources.Res
import clock_always_on.shared.generated.resources.ic_animation_off
import clock_always_on.shared.generated.resources.ic_animation_on
import clock_always_on.shared.generated.resources.ic_battery_off
import clock_always_on.shared.generated.resources.ic_battery_on
import clock_always_on.shared.generated.resources.ic_color_fill
import clock_always_on.shared.generated.resources.ic_color_text
import clock_always_on.shared.generated.resources.ic_date_off
import clock_always_on.shared.generated.resources.ic_date_on
import clock_always_on.shared.generated.resources.ic_timer
import clock_always_on.shared.generated.resources.ic_timer_off
import org.jetbrains.compose.resources.painterResource

private val buttonSize = 36.dp
private val buttonSpace = 4.dp
private val buttonColor = Color.White.copy(alpha = 0.8f)

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
    var showBottomSheet by remember { mutableStateOf(false) }

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
        IconButtonTextColor(configuration.textColor, onClick = { showBottomSheet = true })
        Spacer(modifier = Modifier.width(buttonSpace))
        IconButtonBackgroundColor(
            configuration.backgroundColor,
            onClick = listener.onBackgroundClick
        )
    }
    if (showBottomSheet) {
        ColorPickerDialog(
            onColorSelected = {
                showBottomSheet = false
                listener.onTextColorClick(it)
            },
            onDismissRequest = { showBottomSheet = false }
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
            textAlign = TextAlign.End,
            color = buttonColor
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
                painterResource(Res.drawable.ic_timer)
            else
                painterResource(Res.drawable.ic_timer_off),
            tint = buttonColor,
            contentDescription = "seconds"
        )
    }
}

@Composable
private fun IconButtonBounce(selected: Boolean, onClick: BooleanCallback) {
    IconButton(
        onClick = { onClick.invoke(!selected) }
    ) {
        Icon(
            modifier = Modifier.size(buttonSize),
            painter = if (selected)
                painterResource(Res.drawable.ic_animation_on)
            else
                painterResource(Res.drawable.ic_animation_off),
            tint = buttonColor,
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
                painterResource(Res.drawable.ic_date_on)
            else
                painterResource(Res.drawable.ic_date_off),
            tint = buttonColor,
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
                painterResource(Res.drawable.ic_battery_on)
            else
                painterResource(Res.drawable.ic_battery_off),
            tint = buttonColor,
            contentDescription = "battery"
        )
    }
}

@Composable
private fun IconButtonTextColor(color: Color, onClick: ColorCallback) {
    IconButton(
        onClick = { onClick.invoke(color) }
    ) {
        Icon(
            modifier = Modifier.size(buttonSize),
            painter = painterResource(Res.drawable.ic_color_text),
            tint = buttonColor,
            contentDescription = "text color"
        )
    }
}

private val backgroundColors = arrayOf(
    Color.Black,
    Color.DarkGray,
    Color(0xFF021526),
    Color(0xFF070F2B),
    Color(0xFF090040),
    Color(0xFF09122C),
    Color(0xFF0C0C0C),
    Color(0xFF0F0F0F),
    Color(0xFF17153B),
    Color(0xFF181C14),
    Color(0xFF18230F),
    Color(0xFF1A120B),
    Color(0xFF1A1A1A),
    Color(0xFF1D1616),
    Color(0xFF1E201E),
    Color(0xFF210F37),
    Color(0xFF22092C),
    Color(0xFF222831),
    Color(0xFF3A0519),
    Color.Transparent
)

@Composable
private fun IconButtonBackgroundColor(color: Color, onClick: ColorCallback) {
    val colorIndex = backgroundColors.indexOf(color)

    IconButton(
        onClick = {
            val colorIndexNext = (colorIndex + 1) % backgroundColors.size
            val colorNext = backgroundColors[colorIndexNext]
            onClick.invoke(colorNext)
        }
    ) {
        Icon(
            modifier = Modifier.size(buttonSize),
            painter = painterResource(Res.drawable.ic_color_fill),
            tint = buttonColor,
            contentDescription = "background color"
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun DefaultPreview() {
//    val configuration = ClockConfiguration()
//    val listener = object : SettingsBarListener {
//        override val on24HourClick: BooleanCallback = {}
//        override val onSecondsClick: BooleanCallback = {}
//        override val onDateClick: BooleanCallback = {}
//        override val onBatteryClick: BooleanCallback = {}
//        override val onBounceClick: BooleanCallback = {}
//        override val onTextColorClick: ColorCallback = {}
//        override val onBackgroundClick: ColorCallback = {}
//    }
//    ClockTheme {
//        SettingsBar(configuration = configuration, listener = listener)
//    }
//}
