package com.plcoding.noteappkmm.domain.time

import kotlinx.datetime.*

// IT is pain to work with Kotlins LocalDateTime so create this Util class to convert DateTime

object DateTimeUtil {

    // get current dateTime according to the time zone
    fun now(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    // get a timeStamp so milliseconds
    fun toEpochMillis(dateTime: LocalDateTime): Long {
        return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    // Formate dateTime to for our NoteModel to a String
    // Kotlin dateTime does not support formatting yet
    fun formatNoteDate(dateTime: LocalDateTime): String {
        val month = dateTime.month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
        // if less than 10 add 0 we want 08 not just 8
        val day = if(dateTime.dayOfMonth < 10) "0${dateTime.dayOfMonth}" else dateTime.dayOfMonth
        val year = dateTime.year
        // same case with the 0
        val hour = if(dateTime.hour < 10) "0${dateTime.hour}" else dateTime.hour
        val minute = if(dateTime.minute < 10) "0${dateTime.minute}" else dateTime.minute

        return buildString {
            append(month)
            append(" ")
            append(day)
            append(" ")
            append(year)
            append(", ")
            append(hour)
            append(":")
            append(minute)
        }
    }
}