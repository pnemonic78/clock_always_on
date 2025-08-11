package pnemonic.clock_always_on

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

internal val deltaBounce = 5.dp
internal var deltaBouncePx = 0f

data class Bounce(
    val tick: Long = 0,
    val screenSize: IntSize = IntSize.Zero,
    val offset: IntOffset = IntOffset.Zero,
    val box: Rect = Rect.Zero,
    val deltaX: Float = 0f,
    val deltaY: Float = 0f
)
