package com.plcoding.noteappkmm.domain.note

// Abstraction for reading notes
// DAO, shared between.., the specification of all CRUD ops. functions

interface NoteDataSource {
    suspend fun insertNote(note: Note)

    suspend fun getNoteById(id: Long): Note?

    // we can use a Flow that will be triggered everytime there is a change in DB
    // we can use Flows in iOS but it requires a lot of setUp because iOS does not know coroutines like Kotlin does (specify Extra dispachers, Flow datatypes...)
    suspend fun getAllNotes(): List<Note>

    suspend fun deleteNoteById(id: Long)
}