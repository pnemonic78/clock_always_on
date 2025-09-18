package pnemonic.clock_always_on

import androidx.compose.ui.graphics.Color

typealias BooleanCallback = (Boolean) -> Unit
typealias ColorCallback = (Color) -> Unit
typealias IntCallback = (Int) -> Unit
typealias VoidCallback = () -> Unit
/**
 * Constant by which to multiply an angular value in degrees to obtain an
 * angular value in radians.
 */
private const val DEGREES_TO_RADIANS = 0.017453292519943295

fun Double.toRadians(): Double = this * DEGREES_TO_RADIANS
