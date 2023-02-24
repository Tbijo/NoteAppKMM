import SwiftUI
import shared

struct NoteDetailScreen: View {
    
    private var noteDataSource: NoteDataSource
    private var noteId: Int64? = nil
    
    @StateObject var viewModel = NoteDetailViewModel(noteDataSource: nil)
    
    // @Environment is comparable to Context in Android
    // gives us references to many thing we want the current screen
    // now we can dismiss the current screen
    // \.presentationMode - equivalent of BackStack in Android
    // use presentation variable to navigate back
    @Environment(\.presentationMode) var presentation
    
    init(noteDataSource: NoteDataSource, noteId: Int64? = nil) {
        self.noteDataSource = noteDataSource
        self.noteId = noteId
    }
    
    var body: some View {
        VStack(alignment: .leading) {

            TextField("Enter a title...", text: $viewModel.noteTitle)
                .font(.title)

            TextField("Enter some content...", text: $viewModel.noteContent)

            Spacer()
        // toolbar for back button
        }.toolbar(content: {
            // content should be a view
            // which will be our Icon Button with check mark
            Button(action: {
                viewModel.saveNote {
                    // if not was successfuly saved go back
                    self.presentation.wrappedValue.dismiss()
                }
            }) {
                Image(systemName: "checkmark")
            }
        })
        .padding()
        .background(Color(hex: viewModel.noteColor))
        .onAppear {
            // when this view appears then we init viewModel and load a note
            viewModel.setParamsAndLoadNote(noteDataSource: noteDataSource, noteId: noteId)
        }
    }
}

struct NoteDetailScreen_Previews: PreviewProvider {
    static var previews: some View {
        EmptyView()
    }
}
