package com.plcoding.noteappkmm.di

import com.plcoding.noteappkmm.data.local.DatabaseDriverFactory
import com.plcoding.noteappkmm.data.note.SqlDelightNoteDataSource
import com.plcoding.noteappkmm.database.NoteDatabase
import com.plcoding.noteappkmm.domain.note.NoteDataSource

class DatabaseModule {

    // this is a manual dependency injection for NoteDataSource in iOS module

    // DatabaseDriverFactory() is needed to construct NoteDataSource
    private val factory by lazy { DatabaseDriverFactory() }
    
    // here we create the NoteDataSource
    // similar as a provides function
    val noteDataSource: NoteDataSource by lazy {
        SqlDelightNoteDataSource(NoteDatabase(factory.createDriver()))
    }
}