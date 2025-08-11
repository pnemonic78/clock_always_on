package pnemonic.clock_always_on

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.DateFormat

class ClockViewModel(
    platform: Platform
) : ViewModel(), SettingsBarListener {

    private val scope: CoroutineScope = viewModelScope
    val configData = ClockConfigurationData(platform.getDataStorePreferences())

    val configuration: StateFlow<ClockConfiguration> = configData.configuration().stateIn(
        scope,
        SharingStarted.Lazily,
        ClockConfiguration(
            is24Hours = platform.is24Hours
        )
    )

    private val _bounce = MutableStateFlow<Bounce>(Bounce())
    val bounces: StateFlow<Bounce> = _bounce

    var bounce: Bounce
        get() = _bounce.value
        set(value) {
            _bounce.value = value
            scope.launch { _bounce.emit(value) }
        }

    val batteryState: StateFlow<BatteryState> =
        platform.getBatteryState()
            .filter { configuration.value.isBattery }
            .stateIn(
                scope,
                SharingStarted.Lazily,
                BatteryState()
            )

    fun set24Hours(visible: Boolean) {
        scope.launch { configData.set24Hours(visible) }
    }

    fun setSeconds(visible: Boolean) {
        scope.launch { configData.setSeconds(visible) }
    }

    fun setDate(visible: Boolean) {
        scope.launch { configData.setDate(visible) }
    }

    fun setBattery(visible: Boolean) {
        scope.launch { configData.setBattery(visible) }
    }

    fun setBounce(enabled: Boolean) {
        scope.launch { configData.setBounce(enabled) }
    }

    fun setBackgroundColor(color: Color) {
        scope.launch { configData.setBackgroundColor(color) }
    }

    fun onClockClick(style: Int) {
        scope.launch {
            var styleNext = style + 1
            if (styleNext > ClockStyle.ANALOG) {
                styleNext = ClockStyle.DIGITAL_STACKED_THIN
            }
            configData.setTimeStyle(styleNext)
        }
    }

    fun onDateClick(style: Int) {
        scope.launch {
            var styleNext = style + 1
            if (styleNext > DateFormat.SHORT) {
                styleNext = DateFormat.FULL
            }
            configData.setDateStyle(styleNext)
        }
    }

    override val on24HourClick: BooleanCallback = {
        set24Hours(it)
    }
    override val onSecondsClick: BooleanCallback = {
        setSeconds(it)
    }
    override val onDateClick: BooleanCallback = {
        setDate(it)
    }
    override val onBatteryClick: BooleanCallback = {
        setBattery(it)
    }
    override val onBounceClick: BooleanCallback = {
        setBounce(it)
    }
    override val onTextColorClick: ColorCallback = {
        //TODO show color picker
        //TODO setTextColor(it)
    }
    override val onBackgroundClick: ColorCallback = {
        setBackgroundColor(it)
    }

    fun updateBounce(deltaX: Float, deltaY: Float) {
        bounce = bounce.copy(deltaX = deltaX, deltaY = deltaY)
    }

    fun updateBounce(screenSize: IntSize) {
        bounce = bounce.copy(screenSize = screenSize)
    }

    fun updateBounce(box: Rect) {
        bounce = bounce.copy(box = box)
    }

    fun updateBounce(
        tick: Long,
        offset: IntOffset,
        deltaX: Float,
        deltaY: Float
    ) {
        bounce = bounce.copy(
            tick = tick,
            offset = offset,
            deltaX = deltaX,
            deltaY = deltaY
        )
    }

    fun bounce() {
        val bounce = this.bounce
        var deltaX = bounce.deltaX
        var deltaY = bounce.deltaY
        val screenSize = bounce.screenSize

        var tx = bounce.offset.x + deltaX
        var ty = bounce.offset.y + deltaY

        val b = bounce.box.translate(tx, ty)
        if (deltaX > 0) {
            if (b.right >= screenSize.width) {
                deltaX = -deltaBouncePx
            }
        } else {
            if (b.left <= 0) {
                deltaX = deltaBouncePx
            }
        }
        if (deltaY > 0) {
            if (b.bottom >= screenSize.height) {
                deltaY = -deltaBouncePx
            }
        } else {
            if (b.top <= 0) {
                deltaY = deltaBouncePx
            }
        }

        updateBounce(
            tick = bounce.tick + 1,
            offset = IntOffset(tx.toInt(), ty.toInt()),
            deltaX = deltaX,
            deltaY = deltaY
        )
    }

    fun resetBounce() {
        bounce = Bounce(
            screenSize = bounce.screenSize,
            deltaX = deltaBouncePx,
            deltaY = deltaBouncePx
        )
    }
}