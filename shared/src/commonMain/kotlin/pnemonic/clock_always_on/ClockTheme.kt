package pnemonic.clock_always_on

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ClockTheme(
    content: @Composable () -> Unit
) {
    val colors = darkColorScheme(
        background = Color.Black
    )

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
