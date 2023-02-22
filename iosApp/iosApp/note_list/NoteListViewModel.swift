//
//  NoteListViewModel.swift
//  iosApp
//
//  Created by Philipp Lackner on 26.09.22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared

// extension - similar to extension in Kotlin
// NoteListViewModel will be created only inside NoteListScreen => extension NoteListScreen { class NoteListViewModel }

extension NoteListScreen {

    // ObservableObject (ViewModel) - complx object that hosts state vars that the UI can observe
    // If those states change UI will be notified
    // @MainActor - all code will execute on the main thread (viewmodel should work on main thread)
    @MainActor class NoteListViewModel: ObservableObject {

        // nil = null
        // database
        private var noteDataSource: NoteDataSource? = nil

        // SearchNotes Use Case
        private let searchNotes = SearchNotes()

        // State for normal notes list
        // [Note] - list of type Note, () - creat the list
        private var notes = [Note]()

        // State for filtered notes list
        // private(set) is publicly readable but not modifiable
        // @Published - marks a var to notify UI when it changes for redraw
        @Published private(set) var filteredNotes = [Note]()

        // state for searchText
        @Published var searchText = "" {
            didSet {
                // This block executes after the value of searchText is set
                // When Search Text changes we will update filteredNotes
                self.filteredNotes = searchNotes.execute(notes: self.notes, query: searchText)
            }
        }

        // state for is the search text active
        @Published private(set) var isSearchActive = false

        // init is the constructor
        // noteDataSource with default value nil
        init(noteDataSource: NoteDataSource? = nil) {
            // assign local var
            self.noteDataSource = noteDataSource
        }

        // getAllNotes is a suspend function that is why we get completionHandler
        // completionHandler is a callback that gives us notes or error
        // completionHandler is called when the suspend function is done
        func loadNotes() {
            noteDataSource?.getAllNotes(completionHandler: { notes, error in
                // it can be nil so use ?? (?:) to assign empty list
                self.notes = notes ?? []
                self.filteredNotes = self.notes
            })
        }

        // Int64? = Long?
        func deleteNoteById(id: Int64?) {
            if id != nil {
                // id! = id!!
                noteDataSource?.deleteNoteById(id: id!, completionHandler: { error in
                    // when successful deletion reload notes
                    self.loadNotes()
                })
            }
        }
        
        func toggleIsSearchActive() {
            isSearchActive = !isSearchActive
            if !isSearchActive {
                searchText = ""
            }
        }


        func setNoteDataSource(noteDataSource: NoteDataSource) {
            self.noteDataSource = noteDataSource
        }
    }
}
