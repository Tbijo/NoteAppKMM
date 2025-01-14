import Foundation
import shared

extension NoteDetailScreen {

    @MainActor class NoteDetailViewModel: ObservableObject {
        
        private var noteDataSource: NoteDataSource?
        private var noteId: Int64? = nil
        
        @Published var noteTitle = ""
        @Published var noteContent = ""
        @Published private(set) var noteColor = Note.Companion().generateRandomColor()
        
        init(noteDataSource: NoteDataSource? = nil) {
            self.noteDataSource = noteDataSource
        }
        
        func loadNoteIfExists(id: Int64?) {
            if id != nil {
                self.noteId = id
                noteDataSource?.getNoteById(id: id!, completionHandler: { note, error in
                    self.noteTitle = note?.title ?? ""
                    self.noteContent = note?.content ?? ""
                    self.noteColor = note?.colorHex ?? Note.Companion().generateRandomColor()
                })
            }
        }
        
        // onSaved - when note is saved navigate back
        // @escaping - must be anotated in order to onSaved from completionHandler
        func saveNote(onSaved: @escaping () -> Void) {
            noteDataSource?.insertNote(
                note: Note(
                    id: noteId == nil ? nil : KotlinLong(value: noteId!), 
                    title: self.noteTitle, 
                    content: self.noteContent, 
                    colorHex: self.noteColor, 
                    created: DateTimeUtil().now()
                    ), 
                completionHandler: { error in
                    onSaved()
                })
        }
        
        // set params of this ViewModel
        func setParamsAndLoadNote(noteDataSource: NoteDataSource, noteId: Int64?) {
            self.noteDataSource = noteDataSource
            loadNoteIfExists(id: noteId)
        }
    }
}
