package pnemonic.clock_always_on

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clock_always_on.shared.generated.resources.Res
import clock_always_on.shared.generated.resources.ic_battery_0_bar
import clock_always_on.shared.generated.resources.ic_battery_1_bar
import clock_always_on.shared.generated.resources.ic_battery_2_bar
import clock_always_on.shared.generated.resources.ic_battery_3_bar
import clock_always_on.shared.generated.resources.ic_battery_4_bar
import clock_always_on.shared.generated.resources.ic_battery_5_bar
import clock_always_on.shared.generated.resources.ic_battery_6_bar
import clock_always_on.shared.generated.resources.ic_battery_charging_20
import clock_always_on.shared.generated.resources.ic_battery_charging_30
import clock_always_on.shared.generated.resources.ic_battery_charging_50
import clock_always_on.shared.generated.resources.ic_battery_charging_60
import clock_always_on.shared.generated.resources.ic_battery_charging_80
import clock_always_on.shared.generated.resources.ic_battery_charging_90
import clock_always_on.shared.generated.resources.ic_battery_charging_full
import clock_always_on.shared.generated.resources.ic_battery_full
import clock_always_on.shared.generated.resources.ic_battery_unknown
import org.jetbrains.compose.resources.painterResource
import java.text.NumberFormat

private val iconSize = 24.dp

@Composable
fun BatteryStatus(state: BatteryState, color: Color = Color.Unspecified) {
    val formatter = remember { NumberFormat.getPercentInstance() }

    Row {
        BatteryIcon(level = state.level, charging = state.charging, color = color)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = formatter.format(state.level / 100f), color = color)
    }
}

@Composable
fun BatteryIcon(level: Int, charging: Boolean, color: Color = Color.Unspecified) {
    val painter = when {
        (level < 0) -> painterResource(Res.drawable.ic_battery_unknown)
        charging && (level <= 20) -> painterResource(Res.drawable.ic_battery_charging_20)
        charging && (level < 35) -> painterResource(Res.drawable.ic_battery_charging_30)
        charging && (level < 55) -> painterResource(Res.drawable.ic_battery_charging_50)
        charging && (level < 65) -> painterResource(Res.drawable.ic_battery_charging_60)
        charging && (level < 90) -> painterResource(Res.drawable.ic_battery_charging_80)
        charging && (level < 99) -> painterResource(Res.drawable.ic_battery_charging_90)
        charging -> painterResource(Res.drawable.ic_battery_charging_full)
        (level < 15) -> painterResource(Res.drawable.ic_battery_0_bar)
        (level < 30) -> painterResource(Res.drawable.ic_battery_1_bar)
        (level < 40) -> painterResource(Res.drawable.ic_battery_2_bar)
        (level < 50) -> painterResource(Res.drawable.ic_battery_3_bar)
        (level < 65) -> painterResource(Res.drawable.ic_battery_4_bar)
        (level < 80) -> painterResource(Res.drawable.ic_battery_5_bar)
        (level < 99) -> painterResource(Res.drawable.ic_battery_6_bar)
        else -> painterResource(Res.drawable.ic_battery_full)
    }
    val tint: ColorFilter? = if (color == Color.Unspecified) null else ColorFilter.tint(color)

    Image(
        modifier = Modifier.size(iconSize),
        painter = painter,
        colorFilter = tint,
        contentDescription = "battery $level"
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        Column {
            BatteryStatus(BatteryState(-1, false))
            BatteryStatus(BatteryState(0, false))
            BatteryStatus(BatteryState(5, false))
            BatteryStatus(BatteryState(15, false))
            BatteryStatus(BatteryState(25, false))
            BatteryStatus(BatteryState(35, false))
            BatteryStatus(BatteryState(45, false))
            BatteryStatus(BatteryState(55, false))
            BatteryStatus(BatteryState(65, false))
            BatteryStatus(BatteryState(75, false))
            BatteryStatus(BatteryState(85, false))
            BatteryStatus(BatteryState(95, false))
            BatteryStatus(BatteryState(100, false))

            BatteryStatus(BatteryState(-1, true))
            BatteryStatus(BatteryState(0, true))
            BatteryStatus(BatteryState(5, true))
            BatteryStatus(BatteryState(15, true))
            BatteryStatus(BatteryState(25, true))
            BatteryStatus(BatteryState(35, true))
            BatteryStatus(BatteryState(45, true))
            BatteryStatus(BatteryState(55, true))
            BatteryStatus(BatteryState(65, true))
            BatteryStatus(BatteryState(75, true))
            BatteryStatus(BatteryState(85, true))
            BatteryStatus(BatteryState(95, true))
            BatteryStatus(BatteryState(100, true))
        }
    }
}
