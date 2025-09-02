package pnemonic.clock_always_on

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.DateFormat

class ClockViewModel(
    platform: Platform
) : ViewModel(), SettingsBarListener {

    private val scope: CoroutineScope = viewModelScope
    val configData = ClockConfigurationData(platform.getDataStorePreferences())

    val configuration: StateFlow<ClockConfiguration> = configData.configuration().stateIn(
        scope,
        SharingStarted.Eagerly,
        ClockConfiguration(is24Hours = platform.is24Hours)
    )

    private val _bounce = MutableStateFlow<Bounce>(Bounce())
    val bounces: StateFlow<Bounce> = _bounce
    private var bounceJob: Job? = null

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

    private val _settingsVisible = MutableStateFlow<Boolean>(true)
    val settingsVisible: StateFlow<Boolean> = _settingsVisible
    private var settingsVisibleJob: Job? = null

    init {
        viewModelScope.launch {
            // reading the configuration is not immediate.
            delay(1000)
            if (configuration.value.isBounce) {
                startBounce()
            }
            fadeSettingsBar()
        }
    }

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
        scope.launch {
            configData.setBounce(enabled)
            if (enabled) {
                startBounce()
            } else {
                stopBounce()
            }
        }
    }

    fun setBackgroundColor(color: Color) {
        scope.launch { configData.setBackgroundColor(color) }
    }

    fun setTextColor(color: Color) {
        scope.launch { configData.setTextColor(color) }
    }

    fun onClockClick(style: Int) {
        scope.launch {
            var styleNext = style + 1
            if (styleNext > ClockStyle.ANALOG_TICKS) {
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
    override val onTextColorClick: ColorCallback = { color ->
        if (color != Color.Unspecified) {
            setTextColor(color)
        }
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

    fun startBounce() {
        if (bounceJob?.isActive == true) return
        bounceJob = viewModelScope.launch {
            while (isActive && configuration.value.isBounce) {
                delay(BOUNCE_DELAY)
                bounce()
            }
        }
    }

    private fun bounce() {
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

    fun stopBounce() {
        bounceJob?.cancel()
        bounce = Bounce(
            screenSize = bounce.screenSize,
            deltaX = deltaBouncePx,
            deltaY = deltaBouncePx
        )
    }

    override fun onCleared() {
        super.onCleared()
        bounceJob?.cancel()
        bounceJob = null
        settingsVisibleJob?.cancel()
        settingsVisibleJob = null
    }

    fun onMainViewClick() {
        showSettingsBar()
    }

    fun onSettingsBarClick() {
        showSettingsBar()
    }

    private fun showSettingsBar() {
        settingsVisibleJob?.cancel()
        viewModelScope.launch {
            _settingsVisible.emit(true)
            fadeSettingsBar()
        }
    }

    private fun hideSettingsBar() {
        viewModelScope.launch {
            _settingsVisible.emit(false)
        }
    }

    private fun fadeSettingsBar() {
        settingsVisibleJob = viewModelScope.launch {
            delay(SETTINGS_VISIBILITY_WAIT)
            _settingsVisible.emit(false)
        }
    }

    companion object {
        private const val BOUNCE_DELAY = DateUtils.SECOND_IN_MILLIS
        private const val SETTINGS_VISIBILITY_WAIT = DateUtils.SECOND_IN_MILLIS * 5
        const val SETTINGS_FADE = (DateUtils.SECOND_IN_MILLIS * 5).toInt()
    }
}