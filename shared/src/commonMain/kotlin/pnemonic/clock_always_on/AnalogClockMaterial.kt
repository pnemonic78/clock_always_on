package pnemonic.clock_always_on

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import clock_always_on.shared.generated.resources.Res
import clock_always_on.shared.generated.resources.clock_frame_material
import clock_always_on.shared.generated.resources.hour_hand_material
import clock_always_on.shared.generated.resources.minute_hand_material
import clock_always_on.shared.generated.resources.second_hand_material
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.painterResource

@Composable
fun AnalogClockMaterial(
    time: LocalDateTime,
    modifier: Modifier = Modifier,
    isSeconds: Boolean = false,
    color: Color = Color.Unspecified
) {
    val hours = time.hour
    val minutes = time.minute
    val nanoseconds = time.nanosecond
    val seconds = time.second + (nanoseconds / 1_000_000_000f)
    val hoursAngle = (hours * 360f) / 12f
    val minutesAngle = (minutes * 360f) / 60f
    val secondsAngle = (seconds * 360f) / 60f

    val frame = painterResource(Res.drawable.clock_frame_material)
    val hoursHand = painterResource(Res.drawable.hour_hand_material)
    val minutesHand = painterResource(Res.drawable.minute_hand_material)
    val secondsHand = painterResource(Res.drawable.second_hand_material)
    val tint: ColorFilter? = if (color == Color.Unspecified) null else ColorFilter.tint(color)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .scale(1.15f)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = frame,
            contentDescription = "frame",
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
