package pnemonic.clock_always_on

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController() : UIViewController = ComposeUIViewController {
    MainView()
}