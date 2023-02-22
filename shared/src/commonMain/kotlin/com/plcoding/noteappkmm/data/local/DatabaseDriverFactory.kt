package com.plcoding.noteappkmm.data.local

import com.squareup.sqldelight.db.SqlDriver

// Since we have different dependencies for SQLDelight (iOS native driver and Andriod specific driver)
// So we need to create a way to get the reference to this database in both platforms

// This class will be used to create a specific driver for the specific platform

// since the construction for both platforms differs
// We need to make this class expect
// expect means there needs to be a ACTUAL impl of this driver for both platforms
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

// in order for the IDE to recognize ACTUAL impl we need to make the same dir structure