package pnemonic.clock_always_on

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clock_always_on.shared.generated.resources.Res
import clock_always_on.shared.generated.resources.hour_hand_1
import clock_always_on.shared.generated.resources.minute_hand_1
import clock_always_on.shared.generated.resources.second_hand_1
import org.jetbrains.compose.resources.painterResource
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalogClockTicks(
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

    val hoursHand = painterResource(Res.drawable.hour_hand_1)
    val minutesHand = painterResource(Res.drawable.minute_hand_1)
    val secondsHand = painterResource(Res.drawable.second_hand_1)
    val tint: ColorFilter? = if (color == Color.Unspecified) null else ColorFilter.tint(color)

    Box(modifier = modifier.aspectRatio(1f)) {
        ClockFaceWithTicks(
            color = color
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

private val minuteTickLength = 10.dp
private val minuteTickWidth = 1.dp
private val hourTickLength = 20.dp
private val hourTickWidth = 3.dp

@Composable
private fun ClockFaceWithTicks(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    val colorTicks =
        if (color == Color.Unspecified) MaterialTheme.colorScheme.onBackground else color

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val outerRadius = size.minDimension / 2f

        // Draw minute ticks
        val minuteTickLengthPx = minuteTickLength.toPx()
        val minuteTickWidthPx = minuteTickWidth.toPx()
        val minuteTickRadius = outerRadius - minuteTickLengthPx

        for (i in 0 until 60) {
            val angleInDegrees = i * 6.0
            val angleInRadians = angleInDegrees.toRadians()
            val angleCos = cos(angleInRadians)
            val angleSin = sin(angleInRadians)

            val startX = center.x + minuteTickRadius * angleCos
            val startY = center.y + minuteTickRadius * angleSin
            val startOffset = Offset(startX.toFloat(), startY.toFloat())

            val endX = center.x + outerRadius * angleCos
            val endY = center.y + outerRadius * angleSin
            val endOffset = Offset(endX.toFloat(), endY.toFloat())

            drawLine(
                color = colorTicks,
                start = startOffset,
                end = endOffset,
                strokeWidth = minuteTickWidthPx,
                alpha = 0.9f
            )
        }

        // Draw hour ticks
        val hourTickLengthPx = hourTickLength.toPx()
        val hourTickWidthPx = hourTickWidth.toPx()
        val hourTickRadius = outerRadius - hourTickLengthPx

        for (i in 0 until 12) {
            val angleInDegrees = i * 30.0 // 360 degrees / 12 hours = 30 degrees per hour
            val angleInRadians = angleInDegrees.toRadians()
            val angleCos = cos(angleInRadians)
            val angleSin = sin(angleInRadians)

            val startX = center.x + hourTickRadius * angleCos
            val startY = center.y + hourTickRadius * angleSin
            val startOffset = Offset(startX.toFloat(), startY.toFloat())

            val endX = center.x + outerRadius * angleCos
            val endY = center.y + outerRadius * angleSin
            val endOffset = Offset(endX.toFloat(), endY.toFloat())

            drawLine(
                color = colorTicks,
                start = startOffset,
                end = endOffset,
                strokeWidth = hourTickWidthPx
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    ClockTheme {
        AnalogClockTicks(calendar = Calendar.getInstance())
    }
}
