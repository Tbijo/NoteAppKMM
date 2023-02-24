import SwiftUI
import shared

struct NoteListScreen: View {

    // create data source here (not in viewModel)
    // It is also bound to the life time of this screen
    private var noteDataSource: NoteDataSource
    
    // Initialize viewModel
    // @StateObject - we will have one instance of our viewModel
    // and it will not reinitialize on every screen redraw (recompose)
    @StateObject var viewModel = NoteListViewModel(noteDataSource: nil)
    
    // @State - a normal state (mutableStateOf())
    // isNoteSelected will trigger the navigation
    @State private var isNoteSelected = false
    @State private var selectedNoteId: Int64? = nil
    
    // we will pass noteDataSource from the outside
    init(noteDataSource: NoteDataSource) {
        self.noteDataSource = noteDataSource
    }
    
    var body: some View {
        VStack {
            // ZStack (Box) so in Z direction stacking views on top of eachother
            ZStack {

                NavigationLink(
                    // pass the screen we want to navigate to
                    destination: NoteDetailScreen(
                        noteDataSource: self.noteDataSource, 
                        noteId: selectedNoteId
                    ),
                    // State to trigger navigation
                    isActive: $isNoteSelected
                ) {
                    // it has to contain a view
                    EmptyView()
                }
                // it does not need to be seen
                .hidden()

                // Search field
                // specify generics Screen for navigation
                HideableSearchTextField<NoteDetailScreen>(
                onSearchToggled: {
                    viewModel.toggleIsSearchActive()
                }, 
                destinationProvider: {
                    // give a constructor of the screen
                    NoteDetailScreen(
                        noteDataSource: noteDataSource,
                        noteId: selectedNoteId
                    )
                }, 
                isSearchActive: viewModel.isSearchActive, 
                searchText: $viewModel.searchText
                )   // modifier, set this view width and height
                    .frame(maxWidth: .infinity, minHeight: 40)
                    .padding()
                
                // do not show title when searching (focus on field)
                if !viewModel.isSearchActive {
                    Text("All notes")
                        .font(.title2)
                }
            }
            
            // LazyColumn
            List {
                // generate items of Notes
                // list of notes, id of every item 
                // \.self.id - we are referencing every item and we want to get the id variable of that item
                ForEach(viewModel.filteredNotes, id: \.self.id) { note in
                    // to make items in list clickable wrap them in a button
                    Button(action: {
                        // Trigger the navigation, select a note id
                        isNoteSelected = true
                        selectedNoteId = note.id?.int64Value
                    }) {
                        NoteItem(note: note, onDeleteClick: {
                            // convert KotlinLong to int64
                            viewModel.deleteNoteById(id: note.id?.int64Value)
                        })
                    }
                }
            }
            .onAppear {
                // when the list appears we want to load our notes
                viewModel.loadNotes()
            }
            // style to plain because otherwise it puts items into some boxes (we do not want that)
            .listStyle(.plain)
            // no separators
            .listRowSeparator(.hidden)
        }
        .onAppear {
            // this block is called when this View is drawn on the UI
            // at this point we know what noteDataSource is because the init block was called
            // and the outside value was assign properly
            viewModel.setNoteDataSource(noteDataSource: noteDataSource)
        }
    }
}

struct NoteListScreen_Previews: PreviewProvider {
    static var previews: some View {
        EmptyView()
    }
}
