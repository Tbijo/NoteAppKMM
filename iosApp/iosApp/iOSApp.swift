import SwiftUI
import shared

// This is the entry point of our Application

@main
struct iOSApp: App {

    // whatever we declare in this App class becomes a Singleton
    
    // ref to NoteDataSource
    private let databaseModule = DatabaseModule()
    
	var body: some Scene {
		WindowGroup {
            // Views with navigation should be inside NavigationView
            // just like NavHost
            NavigationView {
                NoteListScreen(noteDataSource: databaseModule.noteDataSource)
            }
            // Changes color of navigation buttons
            .accentColor(.black)
		}
	}
}
