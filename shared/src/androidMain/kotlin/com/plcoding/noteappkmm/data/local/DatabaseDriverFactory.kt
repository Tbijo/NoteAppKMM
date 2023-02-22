package com.plcoding.noteappkmm.data.local

import android.content.Context
import com.plcoding.noteappkmm.database.NoteDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

// ACTUAL implementation of EXPECT DatabaseDriverFactory from commonMain

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        // driver for Android SQLDelight
        return AndroidSqliteDriver(NoteDatabase.Schema, context, "note.db")
    }
}