import SwiftUI

@main
struct iOSApp: App {
	var body: some Scene {
		WindowGroup {
			ContentView()
			.onAppear {
				// Disable the idle timer when the view appears
				UIApplication.shared.isIdleTimerDisabled = true
			}
			.onDisappear {
				// Re-enable the idle timer when the view disappears
				UIApplication.shared.isIdleTimerDisabled = false
			}
		}
	}
}