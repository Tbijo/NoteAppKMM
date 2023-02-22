package com.plcoding.noteappkmm.data.local

import com.plcoding.noteappkmm.database.NoteDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

// ACTUAL implementation of EXPECT DatabaseDriverFactory from commonMain

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        // driver for iOS SQLDelight
        return NativeSqliteDriver(NoteDatabase.Schema, "note.db")
    }
}