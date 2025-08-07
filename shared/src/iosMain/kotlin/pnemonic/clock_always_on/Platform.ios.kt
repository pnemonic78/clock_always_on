package pnemonic.clock_always_on

import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val is24Hours: Boolean get() = TODO()
}

//actual fun getPlatform(): Platform = IOSPlatform()

@Composable
actual fun rememberPlatform(): Platform {
    return remember { IOSPlatform() }
}