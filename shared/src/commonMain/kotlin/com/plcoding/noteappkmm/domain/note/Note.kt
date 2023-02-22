package com.plcoding.noteappkmm.domain.note

import com.plcoding.noteappkmm.presentation.*
import kotlinx.datetime.LocalDateTime

// Note domain model
// this will be shared between..
// all other layers are allowed to access this model
// this model will be used to map from NoteEntity

data class Note(
    val id: Long?,
    val title: String,
    val content: String,
    val colorHex: Long,
    // kotlin dateTime
    val created: LocalDateTime
) {
    companion object {
        // colors from presentation layer
        private val colors = listOf(RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex)

        fun generateRandomColor() = colors.random()
    }
}
