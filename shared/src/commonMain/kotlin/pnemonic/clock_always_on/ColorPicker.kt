package pnemonic.clock_always_on

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

private val cellSize = 32.dp

@Composable
fun ColorPickerGrid(
    modifier: Modifier = Modifier,
    onColorSelected: ColorCallback
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        var h = 0f
        (1..17).forEach {
            var s = 1f
            var v = 0.5f
            Row {
                (1..10).forEach {
                    val color = Color.hsv(h, s, v)
                    ColorCell(
                        modifier = Modifier.size(cellSize),
                        color = color,
                        onColorSelected = onColorSelected
                    )
                    s = max(s - 0.1f, 0f)
                    v = min(v + 0.1f, 1f)
                }
            }
            h = min(h + 20f, 360f)
        }
    }
}

@Composable
private fun ColorCell(
    modifier: Modifier = Modifier,
    color: Color,
    onColorSelected: ColorCallback
) {
    Box(
        modifier = modifier
            .background(color = color)
            .clickable {
                onColorSelected(color)
            })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerDialog(onColorSelected: ColorCallback, onDismissRequest: VoidCallback) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        ColorPickerGrid(
            modifier = Modifier.fillMaxSize(),
            onColorSelected = {
                scope.launch { sheetState.hide() }
                onColorSelected(it)
            })
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        ColorPickerGrid(onColorSelected = {
            println("selected $it")
        })
    }
}
