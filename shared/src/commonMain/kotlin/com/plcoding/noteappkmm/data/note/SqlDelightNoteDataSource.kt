package com.plcoding.noteappkmm.data.note

import com.plcoding.noteappkmm.database.NoteDatabase
import com.plcoding.noteappkmm.domain.note.Note
import com.plcoding.noteappkmm.domain.note.NoteDataSource
import com.plcoding.noteappkmm.domain.time.DateTimeUtil

// A specific implementation of NoteDataSource that uses SqlDelight

class SqlDelightNoteDataSource(
    // Access to database
    db: NoteDatabase
) : NoteDataSource {

    // Access to db CRUD ops. functions
    private val queries = db.noteQueries

    override suspend fun insertNote(note: Note) {
        queries.insertNote(
            id = note.id,
            title = note.title,
            content = note.content,
            colorHex = note.colorHex,
            created = DateTimeUtil.toEpochMillis(note.created)
        )
    }

    override suspend fun getNoteById(id: Long): Note? {
        return queries
            // returns Query<>
            .getNoteById(id)
            // maps it to Query to our NoteEntity if exists otherwise null
            .executeAsOneOrNull()
            // map to our domain model Note
            ?.toNote()
    }

    override suspend fun getAllNotes(): List<Note> {
        return queries
            .getAllNotes()
            .executeAsList()
            .map { it.toNote() }
    }

    override suspend fun deleteNoteById(id: Long) {
        queries.deleteNoteById(id)
    }
}