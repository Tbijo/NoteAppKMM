package com.plcoding.noteappkmm.domain.note

import com.plcoding.noteappkmm.domain.time.DateTimeUtil

// USE CASE

class SearchNotes {

    fun execute(notes: List<Note>, query: String): List<Note> {
        // if search is not specified return all notes
        if(query.isBlank()) {
            return notes
        }
        // otherwise filter according to the title
        return notes.filter {
            it.title.trim().lowercase().contains(query.lowercase()) ||
                    it.content.trim().lowercase().contains(query.lowercase())
        }.sortedBy {
            // sorting should be a separate function according to Single in SOLID
            DateTimeUtil.toEpochMillis(it.created)
        }
    }
}