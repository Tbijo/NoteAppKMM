import Foundation
import SwiftUI

// Just a Swift file

// a function that creates an extension to Color
extension Color {
    // so we created another constructor for Color
    // from hex: Int64 (our Long) we will generate a color through bit shifting
    init(hex: Int64, alpha: Double = 1) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: alpha
        )
    }
}

