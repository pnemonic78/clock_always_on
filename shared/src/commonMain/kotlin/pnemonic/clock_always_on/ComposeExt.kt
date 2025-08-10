package pnemonic.clock_always_on

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset

typealias BooleanCallback = (Boolean) -> Unit
typealias ColorCallback = (Color) -> Unit
typealias IntCallback = (Int) -> Unit
typealias VoidCallback = () -> Unit

fun Offset.toIntOffset(): IntOffset = IntOffset(x.toInt(), y.toInt())
