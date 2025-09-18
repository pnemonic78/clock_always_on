import SwiftUI
import UIKit
import shared

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        // Call your Kotlin function that returns the UIViewController containing the Compose Multiplatform content
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Update the UIViewController if needed (e.g., when data changes)
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}