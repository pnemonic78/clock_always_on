package pnemonic.clock_always_on

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import clock_always_on.shared.generated.resources.Res
import clock_always_on.shared.generated.resources.clock_frame_material
import clock_always_on.shared.generated.resources.hour_hand_material
import clock_always_on.shared.generated.resources.minute_hand_material
import clock_always_on.shared.generated.resources.second_hand_material
import org.jetbrains.compose.resources.painterResource
import java.util.Calendar

@Composable
fun AnalogClockMaterial(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    isSeconds: Boolean = false,
    color: Color = Color.Unspecified
) {
    val hours = calendar.get(Calendar.HOUR)
    val hoursAngle = (hours * 360f) / 12f
    val minutes = calendar.get(Calendar.MINUTE)
    val minutesAngle = (minutes * 360f) / 60f
    val seconds = calendar.get(Calendar.SECOND)
    val secondsAngle = (seconds * 360f) / 60f

    val frame = painterResource(Res.drawable.clock_frame_material)
    val hoursHand = painterResource(Res.drawable.hour_hand_material)
    val minutesHand = painterResource(Res.drawable.minute_hand_material)
    val secondsHand = painterResource(Res.drawable.second_hand_material)
    val tint: ColorFilter? = if (color == Color.Unspecified) null else ColorFilter.tint(color)

    Box(modifier = modifier) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = frame, contentDescription = "frame",
            colorFilter = tint
        )
        Image(
            modifier = Modifier
                .fillMaxSize()
                .rotate(hoursAngle),
            painter = hoursHand,
            colorFilter = tint,
            alpha = 0.9f,
            contentDescription = "$hours hours"
        )
        Image(
            modifier = Modifier
                .fillMaxSize()
                .rotate(minutesAngle),
            painter = minutesHand,
            colorFilter = tint,
            alpha = 0.9f,
            contentDescription = "$minutes minutes"
        )
        if (isSeconds) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(secondsAngle),
                painter = secondsHand,
                colorFilter = tint,
                alpha = 0.9f,
                contentDescription = "$seconds seconds"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ClockTheme {
        AnalogClockMaterial(calendar = Calendar.getInstance())
    }
}
